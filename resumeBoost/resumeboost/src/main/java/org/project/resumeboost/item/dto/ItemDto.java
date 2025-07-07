package org.project.resumeboost.item.dto;

import java.time.LocalDateTime;

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
public class ItemDto {

  private Long id;

  private String category;

  private int itemPrice;

  private Long memberId;
  
  private MemberEntity memberEntity;

  private LocalDateTime createTime;
  private LocalDateTime updateTime;
}
