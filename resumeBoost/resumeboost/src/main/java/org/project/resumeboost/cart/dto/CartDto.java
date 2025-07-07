package org.project.resumeboost.cart.dto;

import java.time.LocalDateTime;
import java.util.List;

import org.project.resumeboost.itemList.entity.ItemListEntity;
import org.project.resumeboost.member.entity.MemberEntity;

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
public class CartDto {
  private Long id;

  private Long memberId;
  
  private MemberEntity memberEntity;

  private List<ItemListEntity> itemListEntities;

  private LocalDateTime createTime;
  private LocalDateTime updateTime;
}
