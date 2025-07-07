package org.project.resumeboost.security.filter;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

import org.project.resumeboost.basic.common.Role;
import org.project.resumeboost.member.entity.MemberEntity;
import org.project.resumeboost.security.CustomMyUserDetails;
import org.project.resumeboost.util.JWTUtil;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import com.google.gson.Gson;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

// JWT 토큰으로 페이지 접속 권한을 기능 구현 // 토큰 검증으로 페이지 접속 여부 
public class JWTCheckFilter extends OncePerRequestFilter { // OncePerRequestFilter: 주로 모든 요청에 대해 체크할 때 // spring
                                                           // security filter

  @Override // shouldNotFilter : 체크하지 않을 것들 & 경로 확인
  protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {

    // Preflight 요청은 체크하지 않음
    // Preflight : 예비 요청 (유효한지) (리소스 낭비를 피하기 위해)
    if (request.getMethod().equals("OPTIONS")) {
      return true;
    }

    String path = request.getRequestURI();

    System.out.println("check uri ......................... " + path);

    // /member/login 은 체크하지 않음
    if (path.equals("/member/login")) {
      return true;
    }
    if (path.equals("/member/insert")) {
      return true;
    }
    if (path.equals("/member/kakaoJoin")) {
      return true;
    }
    if (path.equals("/member/checkEmail")) {
      return true;
    }
    if (path.equals("/member/checkNickName")) {
      return true;
    }
    if (path.equals("/member/insert/mentor")) {
      return true;
    }
    if (path.startsWith("/api/member/")) {
      return true;
    }
    if (path.contains("/board/boardList")) {
      return true;
    }
    if (path.contains("/member/mentorList")) {
      return true;
    }
    if (path.contains("/review/reviewList")) {
      return true;
    }
    if (path.contains("/member/memberDetail")) {
      return true;
    }
    if (path.contains("/api/work24")) {
      return true;
    }
    if (path.startsWith("https://lhsbucket98.s3.ap-northeast-2.amazonaws.com/images/")) {
      return true;
    }
    if (path.contains("jpg") || path.contains("png") || path.contains("pdf") || path.contains("PNG")
        || path.contains("PDF") || path.contains("JPG")) {
      return true;
    }

    return false;
  }

  @Override // shouldNot filter 의 경로가 아닌 경우 필터로 체크
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {

    System.out.println("--------------------------------------------");
    System.out.println("--------------------------------------------");
    System.out.println("--------------------------------------------");

    // AccessToken 확인 -> validationToken()
    System.out.println("-------------------------JWTCheckFilter-----------");
    String authHeaderStr = request.getHeader("Authorization"); // 헤더에 담은 토큰 값 추출

    System.out.println(authHeaderStr); // Bearer 인코딩된토큰값~~

    try { // try&catch -> 헤더에 토큰 없으면 null -> subString 에서 에러 -> 500번 에러 // 예외처리도
      String accessToken = authHeaderStr.substring(7); // Bearer 부분만 짤라서 token 값만 추출

      Map<String, Object> claims = JWTUtil.validationToken(accessToken); // 토큰 검증

      System.out.println("JWT claims: " + claims); // JWT claims: {role=[Role_MEMBER], userEmail=m1@email.com}

      // 토큰에 담은 정보를 get
      String userEmail = (String) claims.get("userEmail");
      String userPw = (String) claims.get("userPw");
      String role = (String) claims.get("role");

      System.out.println(role);

      System.out.println(role.substring(5, role.length()));
      String role2 = role.substring(5, role.length());

      Role role3 = null;
      if (Role.MEMBER.toString().equals(role2)) {
        role3 = Role.MEMBER;
      } else if (Role.MENTOR.toString().equals(role2)) {
        role3 = Role.MENTOR;
      } else if (Role.ADMIN.toString().equals(role2)) {
        role3 = Role.ADMIN;
      }

      MemberEntity memberEntity = MemberEntity.builder()
          .userEmail(userEmail)
          .role(role3)
          .build();

      CustomMyUserDetails customMyUserDetail = new CustomMyUserDetails(memberEntity);

      System.out.println("-------------------------------------------");
      System.out.println(customMyUserDetail);
      customMyUserDetail.getAuthorities().forEach(el -> System.out.println(el.getAuthority()));

      //
      UsernamePasswordAuthenticationToken authenticationToken // 생성 시 인증 과정 실행 // ADMIN 권한 페이지 -> ADMIN 권한 있을 때 접속 , 없으면
                                                              // 403
          = new UsernamePasswordAuthenticationToken(customMyUserDetail, userPw, customMyUserDetail.getAuthorities()); // principal,
                                                                                                                      // 비번,
                                                                                                                      // 권한

      SecurityContextHolder.getContext().setAuthentication(authenticationToken); // 보안 컨텍스트에 저장
      //

      filterChain.doFilter(request, response); // 필터 통과 ** // 이 코드 없으면 페이지 못 감

    } catch (Exception e) { // AccessToken 유효시간 끝나면 Expired 뜸

      System.out.println("JWT Check Error.......");
      System.out.println(e.getMessage());

      Gson gson = new Gson();
      String msg = gson.toJson(Map.of("error", "ERROR_ACCESS_TOKEN"));

      response.setContentType("application/json");
      PrintWriter printWriter = response.getWriter();
      printWriter.println(msg);
      printWriter.close();

    }

    // try {
    // // Bearer accesstoken...
    // String accessToken = authHeaderStr.substring(7); // exception 처리
    // Map<String, Object> claims = JWTUtil.validationToken(accessToken);

    // System.out.println("JWT claims: " + claims);

    // String userEmail = (String) claims.get("userEmail");
    // String userPw = (String) claims.get("userPw");
    // List<String> role = (List<String>) claims.get("role");

    // Role role2 = null;
    // if (Role.ADMIN.toString().equals(role)) {
    // role2 = Role.ADMIN;
    // } else if (Role.MANAGER.toString().equals(role)) {
    // role2 = Role.MANAGER;
    // } else if (Role.MEMBER.toString().equals(role)) {
    // role2 = Role.MEMBER;
    // }

    // // MemberDto memberDto = MemberDto.builder()
    // // .userEmail(userEmail)
    // // .userPw(userPw)
    // // .role(role2)
    // // .build();

    // MemberEntity memberEntity = MemberEntity.builder()
    // .userEmail(userEmail)
    // .userPw(userPw)
    // .role(role2)
    // .build();

    // CustomMyUserDetails customMyUserDetails = new
    // CustomMyUserDetails(memberEntity);

    // customMyUserDetails.getAuthorities().stream().forEach(el ->
    // System.out.println(">>>>>>>>*****" + el.getAuthority()));

    // UsernamePasswordAuthenticationToken authenticationToken
    // = new UsernamePasswordAuthenticationToken(memberEntity, userPw,
    // customMyUserDetails.getAuthorities()); // ??

    // SecurityContextHolder.getContext().setAuthentication(authenticationToken); //
    // ??

    // } catch(Exception e) { // Access 토큰에 문제 있으면 에러 메세지
    // System.out.println("JWT Check Error............");
    // System.out.println(e.getMessage());

    // Gson gson = new Gson();
    // String msg = gson.toJson(Map.of("error", "ERROR_ACCESS_TOKEN"));

    // response.setContentType("application/json");
    // PrintWriter printWriter = response.getWriter();
    // printWriter.println(msg);
    // printWriter.close();

    // }

  }

}
