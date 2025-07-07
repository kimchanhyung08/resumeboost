package org.project.resumeboost.member.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.project.resumeboost.basic.common.Role;
import org.project.resumeboost.member.dto.MemberDto;
import org.project.resumeboost.member.entity.MemberEntity;
import org.project.resumeboost.member.repository.MemberRepository;
import org.project.resumeboost.member.service.impl.MemberServiceImpl;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {
  private final MemberServiceImpl memberServiceImpl;
  private final MemberRepository memberRepository;

  @PostMapping("/insert")
  public ResponseEntity<?> join(@RequestBody MemberDto memberDto) {

    Map<String, String> map = new HashMap<>();

    memberDto.setRole(Role.MEMBER);

    memberServiceImpl.joinOk(memberDto);

    map.put("member", "성공");

    return ResponseEntity.status(HttpStatus.OK).body(map);
  }

  @PostMapping("/insert/mentor")
  public ResponseEntity<?> joinT(@RequestBody MemberDto memberDto) {

    Map<String, String> map = new HashMap<>();
    System.out.println(memberDto.getUserEmail());
    memberDto.setRole(Role.MENTOR);

    memberServiceImpl.joinOk(memberDto);

    map.put("member", "성공");

    return ResponseEntity.status(HttpStatus.OK).body(map);
  }

  @PostMapping("/kakaoJoin")
  public ResponseEntity<?> kakaoJoin(@RequestBody MemberDto memberDto) {

    Map<String, String> map = new HashMap<>();

    memberServiceImpl.kakaoJoin(memberDto);

    map.put("member", "성공");

    return ResponseEntity.status(HttpStatus.OK).body(map);
  }

  @PostMapping("/checkEmail")
  public ResponseEntity<?> checkEmail(@RequestBody MemberDto memberDto) {
    Optional<MemberEntity> existingEmail = memberRepository.findByUserEmail(memberDto.getUserEmail());
    Map<String, Boolean> response = new HashMap<>();
    response.put("exists", existingEmail.isPresent());
    return ResponseEntity.ok(response);
  }

  @PostMapping("/checkNickName")
  public ResponseEntity<?> checkNickName(@RequestBody MemberDto memberDto) {
    Optional<MemberEntity> existingNickName = memberRepository.findByNickName(memberDto.getNickName());
    Map<String, Boolean> response = new HashMap<>();
    response.put("exists", existingNickName.isPresent());
    return ResponseEntity.ok(response);
  }

  @GetMapping("/mentorDetail/{id}/{myId}")
  public ResponseEntity<?> mentorDetail(@PathVariable("id") Long mentorId, @PathVariable("myId") Long myId) {

    Map<String, MemberDto> map = new HashMap<>();

    MemberDto mentor = memberServiceImpl.mentorDetail(mentorId, myId);

    map.put("mentor", mentor);

    return ResponseEntity.status(HttpStatus.OK).body(map);
  }

  @GetMapping("/memberDetail/{id}")
  public ResponseEntity<?> memberDetail(@PathVariable("id") Long memberId) {

    Map<String, MemberDto> map = new HashMap<>();

    MemberDto memberDto = memberServiceImpl.memberDetail(memberId);

    map.put("member", memberDto);

    return ResponseEntity.status(HttpStatus.OK).body(map);
  }

  @GetMapping("/memberList")
  public ResponseEntity<?> memberList(
      @PageableDefault(page = 0, size = 5, sort = "id", direction = Sort.Direction.ASC) Pageable pageable,
      @RequestParam(name = "subject", required = false) String subject,
      @RequestParam(name = "search", required = false) String search) {

    Page<MemberDto> memberList = memberServiceImpl.memberList(pageable, subject, search);
    Map<String, Object> map = new HashMap<>();

    int totalPages = memberList.getTotalPages(); // 총 페이지수
    int currentPage = memberList.getPageable().getPageNumber();
    int block = 5;

    int startPage = (int) ((Math.floor(currentPage / block) * block) + 1 <= totalPages
        ? (Math.floor(currentPage / block) * block) + 1
        : totalPages);
    int endPage = (startPage + block) - 1 < totalPages ? (startPage + block) - 1 : totalPages;

    map.put("memberList", memberList);
    map.put("startPage", startPage);
    map.put("endPage", endPage);
    map.put("subject", subject);
    map.put("search", search);

    return ResponseEntity.status(HttpStatus.OK).body(map);
  }

  // 멘토 리스트
  @GetMapping("/mentorList")
  public ResponseEntity<?> mentorList(
      @PageableDefault(page = 0, size = 5, sort = "id", direction = Sort.Direction.ASC) Pageable pageable,
      @RequestParam(name = "subject", required = false) String subject,
      @RequestParam(name = "search", required = false) String search) {
    Map<String, Object> map = new HashMap<>();

    Page<MemberDto> mentorList = memberServiceImpl.mentorList(pageable, subject, search);

    int totalPages = mentorList.getTotalPages(); // 총 페이지수
    int currentPage = mentorList.getPageable().getPageNumber();
    int block = 5;

    int startPage = (int) ((Math.floor(currentPage / block) * block) + 1 <= totalPages
        ? (Math.floor(currentPage / block) * block) + 1
        : totalPages);
    int endPage = (startPage + block) - 1 < totalPages ? (startPage + block) - 1 : totalPages;

    map.put("mentorList", mentorList);
    map.put("startPage", startPage);
    map.put("endPage", endPage);
    map.put("subject", subject);
    map.put("search", search);

    return ResponseEntity.ok().body(map);
  }

  @PutMapping("/modifyA")
  public ResponseEntity<?> modifyA(MemberDto memberDto) throws IOException {

    Map<String, String> map = new HashMap<>();

    memberServiceImpl.modifyOk(memberDto);

    map.put("member", "성공");

    return ResponseEntity.status(HttpStatus.OK).body(map);
  }

  @PostMapping("/modify")
  public ResponseEntity<?> modify(MemberDto memberDto) throws IOException {

    Map<String, String> map = new HashMap<>();

    memberDto.setRole(Role.MEMBER);

    memberServiceImpl.modifyOk(memberDto);

    map.put("member", "성공");

    return ResponseEntity.status(HttpStatus.OK).body(map);
  }

  @PostMapping("/modify/mentor")
  public ResponseEntity<?> modifyT(MemberDto memberDto) throws IOException {

    Map<String, String> map = new HashMap<>();

    memberDto.setRole(Role.MENTOR);

    memberServiceImpl.modifyOk(memberDto);

    map.put("member", "성공");

    return ResponseEntity.status(HttpStatus.OK).body(map);
  }

  @GetMapping("/myDetail/{id}")
  public ResponseEntity<?> myDetail(@PathVariable("id") Long id) {
    Map<String, Object> map = new HashMap<>();

    MemberDto memberDto = memberServiceImpl.modifyMyDetail(id);

    map.put("member", memberDto);

    return ResponseEntity.ok().body(map);
  }

  // 포트폴리오 파일 추가
  @PutMapping("/mentorPtFile")
  public ResponseEntity<?> addPortfolio(MemberDto memberDto) throws IOException {
    Map<String, Object> map = new HashMap<>();

    memberServiceImpl.addPortFolioFile(memberDto);

    map.put("member", "save");

    return ResponseEntity.ok().body(map);
  }

  // 멘토 디테일 추가
  @PutMapping("/mentor/detail")
  public ResponseEntity<?> addDetail(MemberDto memberDto) {
    Map<String, Object> map = new HashMap<>();

    memberServiceImpl.addDetail(memberDto);

    System.out.println(memberDto.getDetail());

    map.put("member", "save");

    return ResponseEntity.ok().body(map);
  }
}
