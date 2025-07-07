package org.project.resumeboost.kakaoPay.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.project.resumeboost.kakaoPay.dto.ReadyResponse;
import org.project.resumeboost.pay.dto.PayDto;
import org.project.resumeboost.itemList.entity.ItemListEntity;
import org.project.resumeboost.itemList.repository.ItemListRepository;
import org.project.resumeboost.kakaoPay.dto.KakaoApproveResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class KakaoPayService {
  @Value("${kakaopay.secret-key}")
  String secretKey;

  @Value("${kakaopay.cid}")
  String cid;

  private ReadyResponse kakaoReady;
  private final ItemListRepository itemListRepository;

  // request header
  private HttpHeaders getHeaders() {
    HttpHeaders headers = new HttpHeaders();
    String auth = "SECRET_KEY " + secretKey;
    headers.set("Authorization", auth);
    headers.set("Content-Type", "application/json");
    return headers;
  }

  // 결제 요청
  public ReadyResponse kakaoPayReady(PayDto payDto) {
    List<ItemListEntity> itemListEntities = itemListRepository.findAllByCartEntityId(payDto.getCartId());
    Map<String, Object> parameters = new HashMap<>();

    parameters.put("cid", cid);
    parameters.put("partner_order_id", payDto.getCartId()); // 가맹점 주문번호
    parameters.put("partner_user_id", payDto.getMemberId()); // 회원 ID

    if (itemListEntities.size() > 1) {
      parameters.put("item_name", "자소서 외" + (itemListEntities.size() - 1));
      parameters.put("quantity", itemListEntities.size());
    } else {
      parameters.put("item_name", itemListEntities.get(0).getItemEntity().getCategory());
      parameters.put("quantity", "1");
    }
    parameters.put("total_amount", payDto.getTotalPrice());
    parameters.put("tax_free_amount", "0");
    parameters.put("approval_url",
        "http://ec2-13-125-236-223.ap-northeast-2.compute.amazonaws.com/pay/addPay/" + payDto.getMemberId()
            + "?cartId=" + payDto.getCartId());
    parameters.put("cancel_url", "http://ec2-13-125-236-223.ap-northeast-2.compute.amazonaws.com:8090/kakao/cancel");
    parameters.put("fail_url", "http://ec2-13-125-236-223.ap-northeast-2.compute.amazonaws.com:8090/kakao/fail");

    HttpEntity<Map<String, Object>> requesEntity = new HttpEntity<>(parameters, getHeaders());

    RestTemplate restTemplate = new RestTemplate();

    kakaoReady = restTemplate.postForObject(
        "https://open-api.kakaopay.com/online/v1/payment/ready",
        requesEntity,
        ReadyResponse.class);

    return kakaoReady;
  }

  // 결제 승인
  public KakaoApproveResponse approveResponse(String pgToken, String tid, String cartId, String memberId) {

    Map<String, String> parameters = new HashMap<>();
    parameters.put("cid", cid);
    parameters.put("tid", tid);
    parameters.put("partner_order_id", cartId);
    parameters.put("partner_user_id", memberId);
    parameters.put("pg_token", pgToken);

    HttpEntity<Map<String, String>> requestEntity = new HttpEntity<>(parameters, getHeaders());

    RestTemplate restTemplate = new RestTemplate();

    KakaoApproveResponse approveResponse = restTemplate.postForObject(
        "https://open-api.kakaopay.com/online/v1/payment/approve",
        requestEntity, KakaoApproveResponse.class);

    return approveResponse;
  }

}
