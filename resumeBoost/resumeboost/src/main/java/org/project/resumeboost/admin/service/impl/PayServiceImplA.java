package org.project.resumeboost.admin.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.project.resumeboost.admin.service.PayServiceA;
import org.project.resumeboost.board.dto.BoardDto;
import org.project.resumeboost.board.entity.BoardEntity;
import org.project.resumeboost.cart.entity.CartEntity;
import org.project.resumeboost.cart.repository.CartRepository;
import org.project.resumeboost.itemList.entity.ItemListEntity;
import org.project.resumeboost.itemList.repository.ItemListRepository;
import org.project.resumeboost.member.entity.MemberEntity;
import org.project.resumeboost.member.repository.MemberRepository;
import org.project.resumeboost.pay.dto.PayDto;
import org.project.resumeboost.pay.entity.OrderItemEntity;
import org.project.resumeboost.pay.entity.PayEntity;
import org.project.resumeboost.pay.repository.OrderItemRepository;
import org.project.resumeboost.pay.repository.PayRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PayServiceImplA implements PayServiceA {

  private final MemberRepository memberRepository;
  private final CartRepository cartRepository;
  private final ItemListRepository itemListRepository;
  private final OrderItemRepository orderItemRepository;
  private final PayRepository payRepository;

  @Override
  public void insertPay(PayDto payDto) {

    // Optional<MemberEntity> optionalMemberEntity =
    // memberRepository.findById(payDto.getMemberId());
    // if(!optionalMemberEntity.isPresent()) {
    // throw new NullPointerException("회원이 존재하지 않습니다!!");
    // }

    Optional<CartEntity> optionalCartEntity = cartRepository.findByMemberEntityId(payDto.getMemberId());
    if (!optionalCartEntity.isPresent()) {
      throw new NullPointerException("장바구니가 존재하지 않습니다!!");
    }

    Long cartId = optionalCartEntity.get().getId();

    List<ItemListEntity> itemEntities = itemListRepository.findAllByCartEntityId(cartId);

    if (itemEntities.isEmpty()) {
      throw new NullPointerException("담은 상품이 없습니다!!");
    }

    PayEntity payEntity = PayEntity.builder()
        .memberEntity(MemberEntity.builder().id(payDto.getMemberId()).build())
        .totalPrice(payDto.getTotalPrice())
        .paymentType(payDto.getPaymentType())
        .build();

    PayEntity payEntity2 = payRepository.save(payEntity);

    itemEntities.forEach(el -> orderItemRepository.save(OrderItemEntity.builder()
        .itemCategory(el.getItemEntity().getCategory())
        .itemPrice(el.getItemEntity().getItemPrice())
        .payEntity(payEntity2)
        .build()));

    cartRepository.deleteById(cartId);

    System.out.println("결제 진행 완료");

  }

  @Override
  public Page<PayDto> payList(Pageable pageable, String subject, String search) {

    Page<PayEntity> payEntities = null;
    // Page<PayEntity> payEntities = payRepository.findAll(pageable);

    if (subject == null & search == null) {
      payEntities = payRepository.findAll(pageable);
    } else if (subject.equals("paymentType")) {
      payEntities = payRepository.findBypaymentTypeContaining(pageable, search);
    } else if (subject.equals("memberId") && !search.equals("")) {
      payEntities = payRepository.findAllByMemberEntityId(pageable, Long.valueOf(search));
    } else {
      payEntities = payRepository.findAll(pageable);
    }

    if (payEntities.isEmpty()) {
      throw new IllegalArgumentException("검색 결과가 조회되지 않습니다.");
    }

    return payEntities.map(el -> PayDto.builder()
        .id(el.getId())
        .createTime(el.getCreateTime())
        .updateTime(el.getUpdateTime())
        .memberEntity(el.getMemberEntity())
        .orderItemEntities(el.getOrderItemEntities())
        .totalPrice(el.getTotalPrice())
        .paymentType(el.getPaymentType())
        .build());
  }

  @Override
  public PayDto payDetail(Long payId) {

    PayEntity payEntity = payRepository.findById(payId)
        .orElseThrow(() -> new NullPointerException("결제정보가 존재하지 않습니다!!"));

    return PayDto.builder()
        .id(payEntity.getId())
        .memberEntity(payEntity.getMemberEntity())
        .memberId(payEntity.getMemberEntity().getId())
        .totalPrice(payEntity.getTotalPrice())
        .paymentType(payEntity.getPaymentType())
        .createTime(payEntity.getCreateTime())
        .updateTime(payEntity.getUpdateTime())
        .orderItemEntities(payEntity.getOrderItemEntities())
        .build();
  }

}
