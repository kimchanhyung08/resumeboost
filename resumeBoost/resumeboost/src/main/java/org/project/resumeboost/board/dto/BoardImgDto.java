package org.project.resumeboost.board.dto;

import org.project.resumeboost.board.entity.BoardEntity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BoardImgDto {
  private Long id;

  private String newImgName;

  private String oldImgName;

  private BoardEntity boardEntity;
}
