package org.project.resumeboost.itemList.entity;

import org.project.resumeboost.basic.common.BasicTime;
import org.project.resumeboost.cart.entity.CartEntity;
import org.project.resumeboost.item.entity.ItemEntity;

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
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "itemList_tb")
public class ItemListEntity extends BasicTime{

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "itemList_id")
  private Long id;

  @ManyToOne(fetch=FetchType.LAZY)
  @JoinColumn(name = "item_id")
  private ItemEntity itemEntity;

  @ManyToOne(fetch=FetchType.LAZY)
  @JoinColumn(name = "cart_id")
  private CartEntity cartEntity;
  
}
