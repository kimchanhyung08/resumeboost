package org.project.resumeboost.pay.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
import org.project.resumeboost.pay.service.PayService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class PayServiceImpl implements PayService {
  private final PayRepository payRepository;
  private final MemberRepository memberRepository;
  private final ItemListRepository itemListRepository;
  private final OrderItemRepository orderItemRepository;
  private final CartRepository cartRepository;

  @Override
  public void addPay(PayDto payDto) {
    MemberEntity memberEntity = memberRepository.findById(payDto.getMemberId())
        .orElseThrow(IllegalArgumentException::new);

    List<ItemListEntity> itemListEntities = itemListRepository.findAllByCartEntityId(payDto.getCartId());

    Long payId = payRepository.save(PayEntity.builder()
        .paymentType(payDto.getPaymentType())
        .totalPrice(payDto.getTotalPrice())
        .memberEntity(memberEntity)
        .build()).getId();

    List<OrderItemEntity> orderItemEntities = itemListEntities.stream().map(item -> OrderItemEntity.builder()
        .itemCategory(item.getItemEntity().getCategory())
        .itemPrice(item.getItemEntity().getItemPrice())
        .payEntity(PayEntity.builder().id(payId).build())
        .build()).collect(Collectors.toList());

    for (OrderItemEntity orderItemEntity : orderItemEntities) {
      orderItemRepository.save(orderItemEntity);
    }
  }

  @Override
  public List<PayDto> myPay(Long id) {

    List<PayEntity> payEntities = payRepository.findAllByMemberEntityId(id);

    Optional<CartEntity> optionalCartEntity = cartRepository.findByMemberEntityId(id);

    if (optionalCartEntity.isPresent()) {
      cartRepository.deleteById(optionalCartEntity.get().getId());
    }

    return payEntities.stream().map(pay -> PayDto.builder()
        .id(pay.getId())
        .paymentType(pay.getPaymentType())
        .totalPrice(pay.getTotalPrice())
        .memberEntity(pay.getMemberEntity())
        .memberId(pay.getMemberEntity().getId())
        .orderItemEntities(pay.getOrderItemEntities())
        .createTime(pay.getCreateTime())
        .updateTime(pay.getUpdateTime())
        .build()).collect(Collectors.toList());
  }

  @Override
  public Page<PayDto> payList(Pageable pageable, String subject, String search) {
    Page<PayEntity> payEntities = null;

    if (subject == null || search == null) {
      payEntities = payRepository.findAll(pageable);
    } else {
      if (subject.equals("paymentType")) {
        payEntities = payRepository.findByPaymentTypeContaining(pageable, search);
      }
    }

    return payEntities.map(pay -> PayDto.builder()
        .id(pay.getId())
        .paymentType(pay.getPaymentType())
        .totalPrice(pay.getTotalPrice())
        .memberEntity(pay.getMemberEntity())
        .memberId(pay.getMemberEntity().getId())
        .orderItemEntities(pay.getOrderItemEntities())
        .createTime(pay.getCreateTime())
        .updateTime(pay.getUpdateTime())
        .build());
  }

}
