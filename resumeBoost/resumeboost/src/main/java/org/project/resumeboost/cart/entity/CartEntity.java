package org.project.resumeboost.cart.entity;

import java.util.List;

import org.project.resumeboost.basic.common.BasicTime;
import org.project.resumeboost.itemList.entity.ItemListEntity;
import org.project.resumeboost.member.entity.MemberEntity;
import org.project.resumeboost.pay.entity.PayEntity;

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
import jakarta.persistence.OneToOne;
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
@Table(name = "cart_tb")
public class CartEntity extends BasicTime {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "cart_id")
  private Long id;

  // 장바구니 회원 ID
  @OneToOne
  @JoinColumn(name = "member_id")
  private MemberEntity memberEntity;

  // 장바구니 아이템
  @OneToMany(fetch = FetchType.LAZY,cascade = CascadeType.REMOVE,mappedBy = "cartEntity")
  @JsonIgnore
  private List<ItemListEntity> itemListEntities;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "pay_id")
  private PayEntity payEntity;
}
