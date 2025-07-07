package org.project.resumeboost.kakaoPay.controller;

import java.util.HashMap;
import java.util.Map;

import org.project.resumeboost.kakaoPay.dto.KakaoApproveResponse;
import org.project.resumeboost.kakaoPay.dto.ReadyResponse;
import org.project.resumeboost.kakaoPay.service.KakaoPayService;
import org.project.resumeboost.pay.dto.PayDto;
import org.project.resumeboost.pay.service.impl.PayServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/kakao")
@RequiredArgsConstructor
public class KakaoPayController {
  private final KakaoPayService kakaoPayService;
  private final PayServiceImpl payServiceImpl;

  @PostMapping("/ready")
  public ReadyResponse readyToKakaoPay(@RequestBody PayDto payDto) {
    return kakaoPayService.kakaoPayReady(payDto);
  }

  @GetMapping("/success")
  public ResponseEntity<?> afterPayRequest(@RequestParam("pg_token") String pgToken,
      @RequestParam("tid") String tid, @RequestParam("partner_order_id") String cartId,
      @RequestParam("partner_user_id") String memberId) {

    KakaoApproveResponse kakaoApproveResponse = kakaoPayService.approveResponse(pgToken, tid, cartId, memberId);

    PayDto payDto = PayDto.builder()
        .paymentType("카카오페이")
        .cartId(Long.valueOf(cartId))
        .memberId(Long.valueOf(memberId))
        .totalPrice(kakaoApproveResponse.getAmount().getTotal()).build();

    payServiceImpl.addPay(payDto);

    Map<String, Object> map = new HashMap<>();
    map.put("kakaoApproveResponse", kakaoApproveResponse);

    return ResponseEntity.status(HttpStatus.OK).body(map);
  }

  @GetMapping("/cancel")
  public void cancel() {

    throw new RuntimeException("결제 취소");
  }

  /**
   * 결제 실패
   */
  @GetMapping("/fail")
  public void fail() {

    throw new RuntimeException("결제 실패");
  }
}
