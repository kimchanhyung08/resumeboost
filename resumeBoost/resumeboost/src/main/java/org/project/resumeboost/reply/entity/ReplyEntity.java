package org.project.resumeboost.reply.entity;

import org.project.resumeboost.basic.common.BasicTime;
import org.project.resumeboost.board.entity.BoardEntity;
import org.project.resumeboost.member.entity.MemberEntity;
import org.project.resumeboost.reply.dto.ReplyDto;

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
@Table(name = "reply_tb")
public class ReplyEntity extends BasicTime {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "reply_id")
  private Long id;

  private String content;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "member_id")
  private MemberEntity memberEntity;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "board_id")
  private BoardEntity boardEntity;

  public static ReplyEntity toInsertReplyEntity(ReplyDto replyDto) {
    return ReplyEntity.builder()
        .content(replyDto.getContent())
        .boardEntity(BoardEntity.builder().id(replyDto.getBoardId()).build())
        .memberEntity(MemberEntity.builder().id(replyDto.getMemberId()).build())
        .build();
  }
}
