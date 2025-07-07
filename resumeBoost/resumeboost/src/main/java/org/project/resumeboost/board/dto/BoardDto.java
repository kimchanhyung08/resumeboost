package org.project.resumeboost.board.dto;

import java.time.LocalDateTime;
import java.util.List;

import org.project.resumeboost.board.entity.BoardEntity;
import org.project.resumeboost.board.entity.BoardImgEntity;
import org.project.resumeboost.member.entity.MemberEntity;
import org.project.resumeboost.reply.entity.ReplyEntity;
import org.springframework.web.multipart.MultipartFile;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class BoardDto {
  private Long id;

  private String title;

  private String content;

  private int viewCount;

  private int replyCount;

  private int attachFile;

  private String category;

  private MultipartFile boardImgFile;

  private String oldImgName; // 원본이미지 이름

  private String newImgName; // 새이미지 이름 -> 암호화

  private Long memberId;

  private String writer;

  private MemberEntity memberEntity;

  private List<ReplyEntity> replyEntities;

  private List<BoardImgEntity> boardImgEntities;

  private LocalDateTime createTime;
  private LocalDateTime updateTime;

  public static BoardDto toBoardDto(BoardEntity boardEntity) {
    // 파일이 있을 경우
    if (boardEntity.getAttachFile() == 1) {
      return BoardDto.builder()
          .id(boardEntity.getId())
          .title(boardEntity.getTitle())
          .content(boardEntity.getContent())
          .category(boardEntity.getCategory())
          .viewCount(boardEntity.getViewCount())
          .attachFile(boardEntity.getAttachFile())
          .boardImgEntities(boardEntity.getBoardImgEntities())
          .newImgName(boardEntity.getBoardImgEntities().get(0).getNewImgName())
          .oldImgName(boardEntity.getBoardImgEntities().get(0).getOldImgName())
          .memberId(boardEntity.getMemberEntity().getId())
          .memberEntity(boardEntity.getMemberEntity())
          .replyCount(boardEntity.getReplyCount())
          .replyEntities(boardEntity.getReplyEntities())
          .createTime(boardEntity.getCreateTime())
          .updateTime(boardEntity.getUpdateTime())
          .writer(boardEntity.getWriter())
          .build();
    }
    // 파일이 없을 경우
    return BoardDto.builder()
        .id(boardEntity.getId())
        .title(boardEntity.getTitle())
        .content(boardEntity.getContent())
        .category(boardEntity.getCategory())
        .viewCount(boardEntity.getViewCount())
        .attachFile(boardEntity.getAttachFile())
        .memberId(boardEntity.getMemberEntity().getId())
        .memberEntity(boardEntity.getMemberEntity())
        .replyCount(boardEntity.getReplyCount())
        .replyEntities(boardEntity.getReplyEntities())
        .createTime(boardEntity.getCreateTime())
        .updateTime(boardEntity.getUpdateTime())
        .writer(boardEntity.getWriter())
        .build();
  }
}
