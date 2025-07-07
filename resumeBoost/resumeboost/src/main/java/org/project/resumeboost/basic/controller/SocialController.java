package org.project.resumeboost.basic.controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import org.project.resumeboost.member.dto.MemberDto;
import org.project.resumeboost.member.service.impl.MemberServiceImpl;
import org.project.resumeboost.util.JWTUtil;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class SocialController {
  private final MemberServiceImpl memberService;

  @GetMapping("/api/member/kakao")
  public Map<String, Object> getMemberFromKakao(@RequestParam("accessToken") String accessToken) {
    MemberDto memberDto = memberService.getKakaoMember(accessToken);

    // Collection<String> collectRoles = new ArrayList<>();
    // collectRoles.add(
    // "ROLE_" + memberDto.getRole().toString());

    Map<String, Object> claims = new HashMap<>();
    claims.put("userEmail", memberDto.getUserEmail());
    claims.put("NickName", memberDto.getNickName());
    claims.put("id", memberDto.getId());
    claims.put("social", memberDto.getSocial());
    System.out.println("social: --------" + memberDto.getSocial());
    claims.put("role", "ROLE_" + memberDto.getRole().toString());

    String JWTaccessToken = JWTUtil.generateToken(claims, 1);
    String JWTrefreshToken = JWTUtil.generateToken(claims, 60 * 24);

    claims.put("accessToken", JWTaccessToken);
    claims.put("refreshToken", JWTrefreshToken);

    return claims;
  }
}
