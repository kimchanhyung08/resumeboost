package org.project.resumeboost.kakaoPay.dto;

import lombok.Data;

@Data
public class ReadyResponse {

  private String tid; // 결제 고유 번호
  private String next_redirect_pc_url; // redirect url
  private String next_redirect_app_url;
  private String next_redirect_mobile_url;
  private String android_app_scheme;
  private String ios_app_scheme;
  private String created_at;
}
