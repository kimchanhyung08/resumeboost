package org.project.resumeboost.member.dto;

import org.project.resumeboost.member.entity.MemberEntity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MemberPtDto {
  private Long id;

  private String newPtName;

  private String oldPtName;

  private MemberEntity memberEntity;
}