package org.project.resumeboost.kakaoPay.dto;

import lombok.Data;

@Data
public class KakaoApproveResponse {
  private String aid; // 요청 고유 번호
  private String tid; // 결제 고유번호
  private String cid; // 가맹정 코드
  private String sid;
  private String partner_order_id;
  private String partner_user_id;
  private String payment_method_type;
  private Amount amount;
  private String card_info;
  private String item_name;
  private String item_code;
  private int quantity;
  private String created_at;
  private String approved_at;
  private String payload;
}
