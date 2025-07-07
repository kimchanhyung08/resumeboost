package org.project.resumeboost.itemList.dto;

import org.project.resumeboost.cart.entity.CartEntity;
import org.project.resumeboost.item.entity.ItemEntity;

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
public class ItemListDto {
  private Long id;

  private ItemEntity itemEntity;

  private CartEntity cartEntity;
}
