package org.project.resumeboost.pay.dto;

import java.time.LocalDateTime;
import java.util.List;

import org.project.resumeboost.cart.entity.CartEntity;
import org.project.resumeboost.member.entity.MemberEntity;
import org.project.resumeboost.pay.entity.OrderItemEntity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PayDto {
  private Long id;
  private String paymentType;
  private int totalPrice;

  private MemberEntity memberEntity;
  private Long memberId;

  private List<CartEntity> cartEntities;
  private Long cartId;

  private List<OrderItemEntity> orderItemEntities;

  private LocalDateTime createTime;
  private LocalDateTime updateTime;
}
