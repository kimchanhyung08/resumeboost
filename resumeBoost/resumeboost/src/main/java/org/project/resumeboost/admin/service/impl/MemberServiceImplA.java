package org.project.resumeboost.admin.service.impl;

import java.io.File;
import java.io.IOException;
import java.lang.StackWalker.Option;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.project.resumeboost.admin.service.MemberServiceA;
import org.project.resumeboost.basic.common.Role;
import org.project.resumeboost.basic.controller.APIRefreshController;
import org.project.resumeboost.board.dto.BoardDto;
import org.project.resumeboost.board.entity.BoardEntity;
import org.project.resumeboost.member.dto.MemberDto;
import org.project.resumeboost.member.dto.MemberImgDto;
import org.project.resumeboost.member.entity.MemberEntity;
import org.project.resumeboost.member.entity.MemberImgEntity;
import org.project.resumeboost.member.entity.MemberPtEntity;
import org.project.resumeboost.member.repository.MemberImgRepository;
import org.project.resumeboost.member.repository.MemberPtRepository;
import org.project.resumeboost.member.repository.MemberRepository;
import org.project.resumeboost.s3.S3UploadService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MemberServiceImplA implements MemberServiceA {

  private final MemberRepository memberRepository;
  private final MemberImgRepository memberImgRepository;
  private final S3UploadService s3UploadService;
  private final MemberPtRepository memberPtRepository;

  @Value("${file.path}")
  String saveFile;

  @Override
  public Page<MemberDto> ListAll(Pageable pageable, String subject, String search) {

    Page<MemberEntity> memberEntities = null;

    if (subject == null & search == null) {
      memberEntities = memberRepository.findAll(pageable);
    } else if (subject.equals("role")) {

      Role role = Role.valueOf(search); // .valueOf() : 문자열에 해당하는 eNum 값 리턴

      memberEntities = memberRepository.findByRole(pageable, role);

    } else if (subject.equals("userName")) {
      memberEntities = memberRepository.findAllByUserNameContaining(pageable, search);
    } else if (subject.equals("nickName")) {
      memberEntities = memberRepository.findAllByNickNameContaining(pageable, search);

    } else if (subject.equals("nickName")) {
      memberEntities = memberRepository.findByNickNameContaining(pageable, search);
    } else if (subject.equals("id") && !search.equals("")) {
      memberEntities = memberRepository.findById(pageable, Long.valueOf(search));

    } else {
      memberEntities = memberRepository.findAll(pageable);
    }

    // else if (subject.equals("title")) {
    // memberEntities = memberRepository.findByTitleContaining(pageable, search);
    // } else if (subject.equals("writer")) {
    // memberEntities = memberRepository.findByWriterContaining(pageable, search);
    // }

    return (memberEntities.map(el -> MemberDto.toMemberDto(el)));

  }

  @Override
  public MemberDto memberDetail(Long id) {

    MemberEntity memberEntity = memberRepository.findById(id)
        .orElseThrow(() -> new NullPointerException("회원이 존재하지 않습니다!"));

    return MemberDto.toMemberDto(memberEntity);

  }

  @Override
  public void memberDelete(Long id) {

    Optional<MemberEntity> optionalMemberEntity = memberRepository.findById(id);
    if (!optionalMemberEntity.isPresent()) {
      throw new NullPointerException("회원이 존재하지 않습니다!!");
    }

    deleteExistingImage(optionalMemberEntity.get().getMemberImgEntities().get(0));
    if (optionalMemberEntity.get().getRole().toString() != "MEMBER") {
      Optional<MemberPtEntity> memPtOptional = memberPtRepository.findByMemberEntity(optionalMemberEntity.get());
      if (memPtOptional.isPresent()) {
        deleteExistingPt(optionalMemberEntity.get().getMemberPtEntities().get(0));
      }
    }

    memberRepository.deleteById(id);

  }

  private void deleteExistingPt(MemberPtEntity existingPtEntity) {
    String existingImgPath = existingPtEntity.getNewPtName();
    s3UploadService.delete("images/" + existingImgPath); // S3에서 기존 이미지 삭제
    memberPtRepository.deleteById(existingPtEntity.getId());
  }

  private void deleteExistingImage(MemberImgEntity existingImgEntity) {
    String existingImgPath = existingImgEntity.getNewImgName();
    s3UploadService.delete("images/" + existingImgPath); // S3에서 기존 이미지 삭제
    memberImgRepository.deleteById(existingImgEntity.getId());
  }

  @Override
  public void memberUpdate(MemberDto memberDto) throws IllegalStateException, IOException {
    System.out.println("폼에서 받은 role 값: ->> " + memberDto.getRole());
    Optional<MemberEntity> optionalMemberEntity = memberRepository.findById(memberDto.getId());
    if (!optionalMemberEntity.isPresent()) {
      throw new NullPointerException("수정할 회원이 존재하지 않습니다!!");
    }

    Optional<MemberImgEntity> optionalMemberImgEntity = memberImgRepository
        .findByMemberEntity(MemberEntity.builder().id(memberDto.getId()).build());

    if (optionalMemberImgEntity.isPresent()) {
      String newImgName = optionalMemberImgEntity.get().getNewImgName();
      String saveFilePath = saveFile + "/member/profile/" + newImgName;

      File deleteFile = new File(saveFilePath); // 이미 있으면 삭제

      if (deleteFile.exists()) {
        deleteFile.delete();
        System.out.println("파일을 삭제 합니다");
      } else {
        System.out.println("파일이 존재하지 않습니다");
      }

      // DB 에서 삭제
      memberImgRepository.deleteById(optionalMemberImgEntity.get().getId());

    }

    System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>" + (memberDto.getRole().equals(Role.MENTOR)));
    if (memberDto.getProfileFile() == null) {

      memberRepository.save(MemberEntity.builder()
          .id(memberDto.getId())
          .userEmail(memberDto.getUserEmail())
          .userPw(memberDto.getUserPw())
          .age(memberDto.getAge())
          .address(memberDto.getAddress())
          .nickName(memberDto.getNickName())
          .userName(memberDto.getUserName())
          .detail(memberDto.getDetail())
          .myReplyCount(memberDto.getMyReplyCount())
          .viewCount(memberDto.getViewCount())
          .myReplyCount(memberDto.getMyReplyCount())
          .myPostCount(memberDto.getMyPostCount())
          .career(memberDto.getCareer())
          .role(memberDto.getRole())
          .attachFile(memberDto.getAttachFile())
          .portfolioFile(memberDto.getPortfolioFile())
          .phone(memberDto.getPhone())
          .boardEntities(memberDto.getBoardEntities())
          .replyEntities(memberDto.getReplyEntities())
          .itemEntities(memberDto.getItemEntities())
          .payEntities(memberDto.getPayEntities())
          .memberImgEntities(memberDto.getMemberImgEntities())
          .memberPtEntities(memberDto.getMemberPtEntities())
          .build());
    } else {
      MultipartFile memberImgFile = memberDto.getProfileFile();
      UUID uuid = UUID.randomUUID();

      String oldImgName = memberImgFile.getOriginalFilename();
      String newImgName = uuid + oldImgName;

      String saveFilePath = saveFile + "/member/profile/" + newImgName;
      memberImgFile.transferTo(new File(saveFilePath));

      memberRepository.save(MemberEntity.builder()
          .id(memberDto.getId())
          .userEmail(memberDto.getUserEmail())
          .userPw(memberDto.getUserPw())
          .age(memberDto.getAge())
          .address(memberDto.getAddress())
          .nickName(memberDto.getNickName())
          .userName(memberDto.getUserName())
          .detail(memberDto.getDetail())
          .myReplyCount(memberDto.getMyReplyCount())
          .viewCount(memberDto.getViewCount())
          .myReplyCount(memberDto.getMyReplyCount())
          .myPostCount(memberDto.getMyPostCount())
          .career(memberDto.getCareer())
          .role(memberDto.getRole())
          .attachFile(1)
          .portfolioFile(memberDto.getPortfolioFile())
          .phone(memberDto.getPhone())
          .boardEntities(memberDto.getBoardEntities())
          .replyEntities(memberDto.getReplyEntities())
          .itemEntities(memberDto.getItemEntities())
          .payEntities(memberDto.getPayEntities())
          .memberImgEntities(memberDto.getMemberImgEntities())
          .memberPtEntities(memberDto.getMemberPtEntities())
          .build());

      MemberImgEntity memberImgEntity = MemberImgEntity.builder()
          .newImgName(newImgName)
          .oldImgName(oldImgName)
          .memberEntity(optionalMemberEntity.get())
          .build();

      memberImgRepository.save(memberImgEntity);

    }

  }

  @Override
  public Page<MemberDto> mentorListAll(Pageable pageable) {

    Page<MemberEntity> mentorList = memberRepository.findByRole(pageable, Role.MENTOR);

    return mentorList.map(el -> MemberDto.toMemberDto(el));
  }

}
