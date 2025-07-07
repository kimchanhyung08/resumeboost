package org.project.resumeboost.pay.entity;

import java.util.List;

import org.project.resumeboost.basic.common.BasicTime;
import org.project.resumeboost.cart.entity.CartEntity;
import org.project.resumeboost.member.entity.MemberEntity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Entity
@Table(name = "pay_tb")
public class PayEntity extends BasicTime {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "pay_id")
  private Long id;

  // 결제방식
  private String paymentType;

  // 총금액
  private int totalPrice;

  // 결제한 회원 ID
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "member_id")
  private MemberEntity memberEntity;

  @OneToMany(mappedBy = "payEntity",fetch = FetchType.LAZY,cascade = CascadeType.REMOVE)
  @JsonIgnore
  private List<CartEntity> cartEntities;

  @OneToMany(mappedBy = "payEntity",fetch = FetchType.LAZY,cascade = CascadeType.REMOVE)
  @JsonIgnore
  private List<OrderItemEntity> orderItemEntities;
}
