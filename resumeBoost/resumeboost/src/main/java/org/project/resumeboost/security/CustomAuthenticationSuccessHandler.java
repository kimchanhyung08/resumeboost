package org.project.resumeboost.security;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import org.project.resumeboost.util.JWTUtil;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

// 시큐리티 성공 
@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

  @Override
  public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
      Authentication authentication) throws IOException, ServletException {

    // JWT
    CustomMyUserDetails userDetails = (CustomMyUserDetails) authentication.getPrincipal();

    System.out.println(">>>>>>>>>>>>>>>>security principal" + userDetails.getUsername());
    // MemberDto memberDto = (MemberDto) authentication.getPrincipal();
    Map<String, Object> claims = new HashMap<>();
    claims.put("userEmail", userDetails.getUsername());
    // claims.put("userPw", userDetails.getPassword());
    claims.put("NickName", userDetails.getNickName()); // ??
    claims.put("id", userDetails.getMemberId());
    claims.put("social", userDetails.isSocial());
    String role = userDetails.getAuthorities().stream()
        .map(el -> el.getAuthority())
        .findFirst() // 첫 번째 값만 가져옴
        .orElse(null); // 값이 없을 경우 null 반환

    claims.put("role", role);
    // claims.put("role", userDetails.getAuthorities().stream().map(el ->
    // el.getAuthority()).collect(Collectors.toList()));

    String accessToken = JWTUtil.generateToken(claims, 1); // 10분
    String refreshToken = JWTUtil.generateToken(claims, 60 * 24); // 24시간

    claims.put("accessToken", accessToken);
    claims.put("refreshToken", refreshToken);

    // ------------------------------토큰 확인
    Gson gson = new Gson();
    String jsonStr = gson.toJson(claims);

    response.setContentType("application/json; charset=UTF-8");
    PrintWriter printWriter = response.getWriter();
    printWriter.println(jsonStr);
    printWriter.close();

    //

    // response.setContentType("text/html;charset=utf-8");
    // PrintWriter out = response.getWriter();

    // out.println("<script> alert('" + authentication.getName() + "님 반갑습니다.');
    // location.href='/'; </script>");

    // out.close();

  }

}
