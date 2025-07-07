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
public class MemberImgDto {
  private Long id;

  private String newImgName;

  private String oldImgName;

  private MemberEntity memberEntity;
}