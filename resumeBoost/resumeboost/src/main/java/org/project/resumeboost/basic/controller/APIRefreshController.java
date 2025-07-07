package org.project.resumeboost.basic.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.project.resumeboost.util.CustomJWTException;
import org.project.resumeboost.util.JWTUtil;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class APIRefreshController {

  @RequestMapping("/api/member/refresh")
  public Map<String, Object> refresh(@RequestHeader("Authorization") String authHeader,
      @RequestParam("refreshToken") String refreshToken) {
    System.out.println("authHeader >>>>>>>>>>" + authHeader);
    System.out.println("refreshToken >>>>>>>>>>" + refreshToken);

    if (refreshToken == null) {
      throw new CustomJWTException("Null_Refresh");
    }
    if (authHeader == null || authHeader.length() < 7) {
      throw new CustomJWTException("INVALID_STRING");
    }

    String accessToken = authHeader.substring(7);

    if (checkExpiredToken(accessToken) == false) {
      return Map.of("accessToken", accessToken, "refreshToken", refreshToken);
    }

    Map<String, Object> claims = JWTUtil.validationToken(refreshToken);

    String newAccessToken = JWTUtil.generateToken(claims, 10);
    String newRefreshToken = checkTime((Integer) claims.get("exp")) == true ? JWTUtil.generateToken(claims, 60 * 24)
        : refreshToken;

    System.out.println("newAccessToken >>>>>>>>>>" + newAccessToken);
    System.out.println("newRefreshToken >>>>>>>>>>" + newRefreshToken);

    // Map<String, Object> map = new HashMap<>();
    // map.put("accessToken", newAccessToken);
    // map.put("refreshToken", newRefreshToken);

    return Map.of("accessToken", newAccessToken, "refreshToken", newRefreshToken);
    // return map;
  }

  private boolean checkTime(Integer exp) {
    Date expDate = new Date((long) exp * (1000));

    long gap = expDate.getTime() - System.currentTimeMillis();

    long leftMin = gap / (1000 * 60);

    return leftMin < 60;
  }

  private boolean checkExpiredToken(String token) {
    try {
      JWTUtil.validationToken(token);
    } catch (CustomJWTException e) {
      if (e.getMessage().equals("Expired")) {
        return true;
      }
    }
    return false;
  }
}