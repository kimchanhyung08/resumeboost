package org.project.resumeboost.member.service;

import java.io.IOException;

import org.project.resumeboost.member.dto.MemberDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MemberService {
  public void joinOk(MemberDto memberDto);

  public MemberDto mentorDetail(Long mentorId, Long myId);

  public MemberDto memberDetail(Long mentorId);

  public Page<MemberDto> memberList(Pageable pageable, String subject, String search);

  public Page<MemberDto> mentorList(Pageable pageable, String subject, String search);

  public MemberDto getKakaoMember(String accessToken);

  public void modifyOk(MemberDto memberDto) throws IOException;

  // 회원정보 수정시 디테일 가져오기
  public MemberDto modifyMyDetail(Long myId);

  // 포트폴리오 추가
  public void addPortFolioFile(MemberDto memberDto) throws IOException;

  // 멘토 디테일 추가
  public void addDetail(MemberDto memberDto);

  // 카카오로그인시 회원정보 추가
  public void kakaoJoin(MemberDto memberDto);
}
