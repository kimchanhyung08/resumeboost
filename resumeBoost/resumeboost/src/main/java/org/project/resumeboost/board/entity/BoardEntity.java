package org.project.resumeboost.board.entity;

import java.util.List;

import org.project.resumeboost.basic.common.BasicTime;
import org.project.resumeboost.board.dto.BoardDto;
import org.project.resumeboost.member.entity.MemberEntity;
import org.project.resumeboost.reply.entity.ReplyEntity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
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
@Table(name = "board_tb")
public class BoardEntity extends BasicTime {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "board_id")
  private Long id;

  @Column(nullable = false)
  private String title;

  @Column(nullable = false, length = 5000)
  private String content;

  @Column(nullable = false)
  private String category;

  @Column(nullable = true, columnDefinition = "int default 0")
  private int viewCount;

  @Column(nullable = true, columnDefinition = "int default 0")
  private int replyCount;

  @Column(nullable = false)
  private int attachFile;

  @Column(nullable = false)
  private String writer;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "member_id")
  private MemberEntity memberEntity;

  @JsonIgnore
  @OneToMany(mappedBy = "boardEntity", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
  private List<ReplyEntity> replyEntities;

  @JsonIgnore
  @OneToMany(mappedBy = "boardEntity", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
  private List<BoardImgEntity> boardImgEntities;

  // 파일 없을 경우
  public static BoardEntity toNotFileInsert(BoardDto dto) {
    return BoardEntity.builder()
        .title(dto.getTitle())
        .content(dto.getContent())
        .category(dto.getCategory())
        .attachFile(0)
        .writer(dto.getWriter())
        .memberEntity(MemberEntity.builder().id(dto.getMemberId()).build())
        .build();
  }

  // 파일 있을 경우
  public static BoardEntity toYesFileInsert(BoardDto dto) {
    return BoardEntity.builder()
        .title(dto.getTitle())
        .content(dto.getContent())
        .category(dto.getCategory())
        .attachFile(1)
        .writer(dto.getWriter())
        .memberEntity(MemberEntity.builder().id(dto.getMemberId()).build())
        .build();
  }

  public static BoardEntity toNotFileUpdate(BoardDto dto) {
    return BoardEntity.builder()
        .id(dto.getId())
        .title(dto.getTitle())
        .content(dto.getContent())
        .category(dto.getCategory())
        .attachFile(0)
        .writer(dto.getWriter())
        .replyCount(dto.getReplyCount())
        .viewCount(dto.getViewCount())
        .memberEntity(MemberEntity.builder().id(dto.getMemberId()).build())
        .build();
  }

  public static BoardEntity toYesFileUpdate(BoardDto dto) {
    return BoardEntity.builder()
        .id(dto.getId())
        .title(dto.getTitle())
        .content(dto.getContent())
        .category(dto.getCategory())
        .attachFile(1)
        .writer(dto.getWriter())
        .replyCount(dto.getReplyCount())
        .viewCount(dto.getViewCount())
        .memberEntity(MemberEntity.builder().id(dto.getMemberId()).build())
        .build();
  }
}
