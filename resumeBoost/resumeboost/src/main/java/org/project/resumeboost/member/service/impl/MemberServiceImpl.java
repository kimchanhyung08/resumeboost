package org.project.resumeboost.member.service.impl;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Optional;

import org.project.resumeboost.basic.common.Role;
import org.project.resumeboost.member.dto.MemberDto;
import org.project.resumeboost.member.entity.MemberEntity;
import org.project.resumeboost.member.entity.MemberImgEntity;
import org.project.resumeboost.member.entity.MemberPtEntity;
import org.project.resumeboost.member.repository.MemberImgRepository;
import org.project.resumeboost.member.repository.MemberPtRepository;
import org.project.resumeboost.member.repository.MemberRepository;
import org.project.resumeboost.member.service.MemberService;
import org.project.resumeboost.review.entity.ReviewEntity;
import org.project.resumeboost.review.repository.ReviewRepository;
import org.project.resumeboost.s3.S3UploadService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberServiceImpl implements MemberService {

  private final MemberRepository memberRepository;
  private final MemberImgRepository memberImgRepository;
  private final MemberPtRepository memberPtRepository;
  private final PasswordEncoder passwordEncoder;
  private final S3UploadService s3UploadService;
  private final ReviewRepository reviewRepository;

  @Value("${file.path}")
  String saveFile;

  @Override
  public void joinOk(MemberDto memberDto) {

    Optional<MemberEntity> email = memberRepository.findByUserEmail(memberDto.getUserEmail());
    Optional<MemberEntity> nickName = memberRepository.findByNickName(memberDto.getNickName());

    if (email.isPresent()) {
      throw new IllegalArgumentException("이미 존재하는 아이디 입니다.");
    } else if (nickName.isPresent()) {
      throw new IllegalArgumentException("이미 존재하는 닉네임 입니다.");
    }

    memberRepository.save(MemberEntity.builder()
        .userEmail(memberDto.getUserEmail())
        .userPw(passwordEncoder.encode(memberDto.getUserPw()))
        .userName(memberDto.getUserName())
        .address(memberDto.getAddress())
        .age(memberDto.getAge())
        .role(memberDto.getRole())
        .phone(memberDto.getPhone())
        .career(memberDto.getCareer())
        .attachFile(0) // 이미지는 회원정보 수정에서 추가
        .portfolioFile(0)
        .nickName(memberDto.getNickName())
        .social(false)
        .build());

  }

  @Override
  public void kakaoJoin(MemberDto memberDto) {
    Optional<MemberEntity> optionalMemberEntity = memberRepository.findByUserEmail(memberDto.getUserEmail());

    if (!optionalMemberEntity.isPresent()) {
      throw new IllegalArgumentException("중복");
    }

    memberRepository.save(MemberEntity.builder()
        .id(optionalMemberEntity.get().getId())
        .userEmail(memberDto.getUserEmail())
        .userPw(passwordEncoder.encode(memberDto.getUserPw()))
        .userName(memberDto.getUserName())
        .address(memberDto.getAddress())
        .age(memberDto.getAge())
        .role(memberDto.getRole())
        .phone(memberDto.getPhone())
        .career(memberDto.getCareer())
        .attachFile(0) // 이미지는 회원정보 수정에서 추가
        .portfolioFile(0)
        .nickName(memberDto.getNickName())
        .social(false)
        .build());
  }

  @Override
  public MemberDto mentorDetail(Long mentorId, Long myId) {
    if (mentorId != myId) {
      mentorViewCount(mentorId);
    }
    MemberEntity mentorEntity = memberRepository.findById(mentorId).orElseThrow(IllegalArgumentException::new);
    return MemberDto.toMemberDto(mentorEntity);
  }

  public void mentorViewCount(Long id) {
    memberRepository.MentorViewCount(id);
  }

  @Override
  public MemberDto memberDetail(Long mentorId) {
    MemberEntity memberEntity = memberRepository.findById(mentorId).orElseThrow(IllegalArgumentException::new);
    return MemberDto.toMemberDto(memberEntity);
  }

  @Override
  public MemberDto modifyMyDetail(Long myId) {
    MemberEntity memberEntity = memberRepository.findById(myId).orElseThrow(IllegalArgumentException::new);
    return MemberDto.toMemberDto(memberEntity);
  }

  @SuppressWarnings("null")
  @Override
  public Page<MemberDto> memberList(Pageable pageable, String subject, String search) {
    Page<MemberEntity> memberPage = null;

    if (subject == null || search == null || search.trim().length() <= 0) {
      memberPage = memberRepository.findAll(pageable);
    } else {
      if (subject.equals("nickName")) {
        memberPage = memberRepository.findByNickNameContaining(pageable, search);
      } else if (subject.equals("address")) {
        memberPage = memberRepository.findByAddressContaining(pageable, search);
      }
    }

    return memberPage.map(MemberDto::toMemberDto);
  }

  @SuppressWarnings("null")
  @Override
  public Page<MemberDto> mentorList(Pageable pageable, String subject, String search) {

    Page<MemberEntity> mentorPage = null;

    if (subject == null || search == null || search.trim().length() <= 0) {
      mentorPage = memberRepository.findByRole(pageable, Role.MENTOR);
    } else {
      if (subject.equals("nickName")) {
        mentorPage = memberRepository.findByRoleAndNickNameContaining(pageable, Role.MENTOR, search);
      } else if (subject.equals("address")) {
        mentorPage = memberRepository.findByRoleAndAddressContaining(pageable, Role.MENTOR, search);
      }
    }

    return mentorPage.map(MemberDto::toMemberDto);
  }

  // kakao 로그인
  @Override
  public MemberDto getKakaoMember(String accessToken) {
    String email = getEmailFromKakaoAccessToken(accessToken);

    Optional<MemberEntity> mOptional = memberRepository.findByUserEmail(email);

    // 기존 회원
    if (mOptional.isPresent()) {
      return MemberDto.toMemberDto(mOptional.get());
    }

    // 신규 회원
    MemberEntity socialMember = makeSocialMember(email);
    memberRepository.save(socialMember);

    return MemberDto.toMemberDto(socialMember);
  }

  private String getEmailFromKakaoAccessToken(String accessToken) {

    String kakaoGetUserURL = "https://kapi.kakao.com/v2/user/me";

    if (accessToken == null) {
      throw new RuntimeException("Access Token is null");
    }
    RestTemplate restTemplate = new RestTemplate();

    HttpHeaders headers = new HttpHeaders();
    headers.add("Authorization", "Bearer " + accessToken);
    headers.add("Content-Type", "application/x-www-form-urlencoded");
    HttpEntity<String> entity = new HttpEntity<>(headers);

    UriComponents uriBuilder = UriComponentsBuilder.fromHttpUrl(kakaoGetUserURL).build();

    ResponseEntity<LinkedHashMap> response = restTemplate.exchange(uriBuilder.toString(), HttpMethod.GET, entity,
        LinkedHashMap.class);

    LinkedHashMap<String, LinkedHashMap> bodyMap = response.getBody();

    LinkedHashMap<String, String> kakaoAccount = bodyMap.get("kakao_account");

    return kakaoAccount.get("email");
  }

  private MemberEntity makeSocialMember(String email) {
    MemberEntity memberEntity = MemberEntity.builder()
        .userEmail(email)
        .nickName("email")
        .userPw(passwordEncoder.encode("1111"))
        .address("address")
        .phone("010-1111-1111")
        .age(20)
        .userName("userName")
        .role(Role.MEMBER)
        .social(true)
        .build();

    return memberEntity;
  }

  @Override
  public void modifyOk(MemberDto memberDto) throws IOException {
    Optional<MemberEntity> meOptional = memberRepository.findByUserEmail(memberDto.getUserEmail());
    Optional<MemberImgEntity> optionalMemImg = memberImgRepository.findByMemberEntity(meOptional.get());

    MultipartFile memberImgFile = memberDto.getProfileFile();
    String existImg = memberDto.getNewImgName();

    String encodedPassword = getEncodedPassword(memberDto, meOptional.get());

    if (isImageUpdateRequired(memberImgFile, existImg)) {
      saveMember(memberDto, meOptional, encodedPassword, 1); // 이미지가 있으므로 attachFile 1
    } else if (isImageNotProvided(memberImgFile, existImg)) {
      saveMember(memberDto, meOptional, encodedPassword, 0); // 이미지가 없으므로 attachFile 0
    } else {
      handleImageUpdate(memberImgFile, optionalMemImg, memberDto, meOptional, encodedPassword);
    }
  }

  private String getEncodedPassword(MemberDto memberDto, MemberEntity existingMember) {
    if (memberDto.getUserPw().equals(existingMember.getUserPw())) {
      return memberDto.getUserPw();
    } else {
      return passwordEncoder.encode(memberDto.getUserPw());
    }
  }

  private boolean isImageUpdateRequired(MultipartFile memberImgFile, String existImg) {
    return (memberImgFile == null || memberImgFile.isEmpty()) && existImg != null;
  }

  private boolean isImageNotProvided(MultipartFile memberImgFile, String existImg) {
    return (memberImgFile == null || memberImgFile.isEmpty()) && existImg == null;
  }

  private void saveMember(MemberDto memberDto, Optional<MemberEntity> meOptional, String encodedPassword,
      int attachFile) {
    memberRepository.save(MemberEntity.builder()
        .id(meOptional.get().getId())
        .userEmail(memberDto.getUserEmail())
        .userPw(encodedPassword)
        .userName(memberDto.getUserName())
        .address(memberDto.getAddress())
        .age(memberDto.getAge())
        .role(memberDto.getRole())
        .phone(memberDto.getPhone())
        .career(memberDto.getCareer())
        .myPostCount(memberDto.getMyPostCount())
        .myReplyCount(memberDto.getMyReplyCount())
        .attachFile(attachFile) // 0 또는 1로 설정
        .portfolioFile(memberDto.getPortfolioFile())
        .nickName(memberDto.getNickName())
        .social(false)
        .detail(memberDto.getDetail())
        .replyCount(memberDto.getReplyCount())
        .viewCount(memberDto.getViewCount())
        .boardEntities(memberDto.getBoardEntities())
        .itemEntities(memberDto.getItemEntities())
        .payEntities(memberDto.getPayEntities())
        .memberPtEntities(memberDto.getMemberPtEntities())
        .replyEntities(memberDto.getReplyEntities())
        .build());
  }

  private void handleImageUpdate(MultipartFile memberImgFile, Optional<MemberImgEntity> optionalMemImg,
      MemberDto memberDto, Optional<MemberEntity> meOptional, String encodedPassword) throws IOException {
    // 기존 이미지 삭제
    if (optionalMemImg.isPresent()) {
      deleteExistingImage(optionalMemImg.get());
    }

    // 새로운 이미지 업로드
    String newImgName = uploadNewImage(memberImgFile);

    // 회원 정보 저장
    saveMemberWithImage(memberDto, meOptional, encodedPassword, newImgName, memberImgFile);
  }

  private void deleteExistingImage(MemberImgEntity existingImgEntity) {
    String existingImgPath = existingImgEntity.getNewImgName();
    s3UploadService.delete("images/" + existingImgPath); // S3에서 기존 이미지 삭제
    memberImgRepository.deleteById(existingImgEntity.getId());
  }

  private String uploadNewImage(MultipartFile memberImgFile) throws IOException {
    String uploadedImageUrl = s3UploadService.upload(memberImgFile, "images");
    return uploadedImageUrl.substring(uploadedImageUrl.indexOf("images/") + "images/".length());
  }

  private void saveMemberWithImage(MemberDto memberDto, Optional<MemberEntity> meOptional, String encodedPassword,
      String newImgName, MultipartFile memberImgFile) {
    Long memberId = memberRepository.save(MemberEntity.builder()
        .id(meOptional.get().getId())
        .userEmail(memberDto.getUserEmail())
        .userPw(encodedPassword)
        .userName(memberDto.getUserName())
        .address(memberDto.getAddress())
        .age(memberDto.getAge())
        .role(memberDto.getRole())
        .phone(memberDto.getPhone())
        .myPostCount(memberDto.getMyPostCount())
        .myReplyCount(memberDto.getMyReplyCount())
        .career(memberDto.getCareer())
        .attachFile(1) // 이미지는 추가되었으므로 1로 설정
        .portfolioFile(memberDto.getPortfolioFile())
        .nickName(memberDto.getNickName())
        .social(false)
        .detail(memberDto.getDetail())
        .replyCount(memberDto.getReplyCount())
        .viewCount(memberDto.getViewCount())
        .boardEntities(memberDto.getBoardEntities())
        .itemEntities(memberDto.getItemEntities())
        .payEntities(memberDto.getPayEntities())
        .memberPtEntities(memberDto.getMemberPtEntities())
        .replyEntities(memberDto.getReplyEntities())
        .build()).getId();

    MemberEntity memberEntity = memberRepository.findById(memberId).orElseThrow(IllegalArgumentException::new);

    // S3에 업로드된 이미지 정보 저장
    memberImgRepository.save(MemberImgEntity.builder()
        .newImgName(newImgName)
        .oldImgName(memberImgFile.getOriginalFilename())
        .memberEntity(memberEntity)
        .build());
  }

  @Override
  public void addPortFolioFile(MemberDto memberDto) throws IOException {
    // 1. 기존 회원 정보 조회
    MemberEntity existingMember = memberRepository.findById(memberDto.getId())
        .orElseThrow(() -> new IllegalArgumentException("회원 정보를 찾을 수 없습니다."));

    // 2. 기존 포트폴리오 파일 조회
    Optional<MemberPtEntity> memPtOptional = memberPtRepository.findByMemberEntity(existingMember);
    String existPt = memberDto.getNewPtName();

    // 3. 새로운 파일 추가 또는 파일 변경
    MultipartFile memberPtFile = memberDto.getPtFile();
    if (memberPtFile != null && !memberPtFile.isEmpty()) {
      if (memPtOptional.isPresent()) {
        // 기존 파일 삭제
        deleteExistingPt(memPtOptional.get());
      }
      String newPtName = uploadNewImage(memberPtFile);
      saveMemberWithPt(existingMember, newPtName, memberPtFile);
    }
  }

  private void saveMemberWithPt(MemberEntity existingMember, String newPtName, MultipartFile memberPtFile) {
    // 1. 기존 회원 정보 유지하면서 portfolioFile 값만 업데이트
    existingMember.setPortfolioFile(1);
    memberRepository.save(existingMember);

    // 2. 새로운 포트폴리오 파일 저장
    memberPtRepository.save(MemberPtEntity.builder()
        .newPtName(newPtName)
        .oldPtName(memberPtFile.getOriginalFilename())
        .memberEntity(existingMember)
        .build());
  }

  private void deleteExistingPt(MemberPtEntity existingPtEntity) {
    String existingImgPath = existingPtEntity.getNewPtName();
    s3UploadService.delete("images/" + existingImgPath); // S3에서 기존 이미지 삭제
    memberPtRepository.deleteById(existingPtEntity.getId());
  }

  @Override
  public void addDetail(MemberDto memberDto) {
    MemberEntity existingMember = memberRepository.findById(memberDto.getId())
        .orElseThrow(() -> new IllegalArgumentException("회원 정보를 찾을 수 없습니다."));
    existingMember.setDetail(memberDto.getDetail());

    memberRepository.save(existingMember);
  }

}
