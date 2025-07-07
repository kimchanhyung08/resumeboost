package org.project.resumeboost.pay.entity;

import org.project.resumeboost.basic.common.BasicTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
@Table(name = "orderItem_tb")
public class OrderItemEntity extends BasicTime{
  
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "orderItem_id")
  private Long id;

  private String itemCategory;

  private int itemPrice;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "pay_id")
  private PayEntity payEntity;
}
