package org.project.resumeboost.member.entity;

import java.util.List;

import org.project.resumeboost.basic.common.BasicTime;
import org.project.resumeboost.basic.common.Role;
import org.project.resumeboost.board.entity.BoardEntity;
import org.project.resumeboost.item.entity.ItemEntity;
import org.project.resumeboost.member.dto.MemberDto;
import org.project.resumeboost.pay.entity.PayEntity;
import org.project.resumeboost.reply.entity.ReplyEntity;
import org.project.resumeboost.review.entity.ReviewEntity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Entity
@ToString(exclude = { "boardEntities", "replyEntities", "itemEntities", "payEntities", "memberImgEntities",
    "memberPtEntities" })
@Table(name = "member_tb")
public class MemberEntity extends BasicTime {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "member_id")
  private Long id;

  @Column(nullable = false, unique = true)
  private String userEmail;

  @Column(nullable = false)
  private String userPw;

  // ?��?��
  @Column(nullable = false)
  private int age;

  // �??��
  @Column(nullable = false)
  private String address;

  // 별명
  @Column(nullable = false, unique = true)
  private String nickName;

  // ?��?�� ?���?
  @Column(nullable = false)
  private String userName;

  // ?��?��?���?
  @Column(length = 5000)
  private String detail;

  // 리뷰?��
  @Column(nullable = true, columnDefinition = "int default 0")
  private int replyCount;

  @Column(nullable = true, columnDefinition = "int default 0")
  private int viewCount;

  // ?���? ?��?��?�� ?���? ?��
  @Column(nullable = true, columnDefinition = "int default 0")
  private int myReplyCount;

  // ?���? ?��?��?�� 게시�? ?��
  @Column(nullable = true, columnDefinition = "int default 0")
  private int myPostCount;

  // 경력
  private String career;

  // 권한 MEMBER(?��반회?��), MENTOR(멘토?��?��), ADMIN
  @Enumerated(EnumType.STRING)
  private Role role;

  // ?��로필 ?���? ?���?
  @Column(nullable = false)
  private int attachFile;

  // ?��?��?��리오 ?���?
  @Column(nullable = false)
  private int portfolioFile;

  @Column(nullable = false)
  private String phone;

  // ?��?��로그?��
  @Column(columnDefinition = "boolean default false")
  private Boolean social;

  // 게시?��
  @JsonIgnore
  @OneToMany(mappedBy = "memberEntity", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
  private List<BoardEntity> boardEntities;

  // 리뷰
  @JsonIgnore
  @OneToMany(mappedBy = "memberEntity", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
  private List<ReplyEntity> replyEntities;//

  // ?��?��
  @OneToMany(mappedBy = "memberEntity", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
  @JsonIgnore
  private List<ItemEntity> itemEntities;

  // 결제
  @JsonIgnore
  @OneToMany(mappedBy = "memberEntity", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
  private List<PayEntity> payEntities;

  // ?��로필?���? ?��?��
  @JsonIgnore
  @OneToMany(mappedBy = "memberEntity", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
  private List<MemberImgEntity> memberImgEntities;

  // ?��?��?��리오 ?��?��
  @JsonIgnore
  @OneToMany(mappedBy = "memberEntity", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
  private List<MemberPtEntity> memberPtEntities;

  @JsonIgnore
  @OneToMany(mappedBy = "memberEntity", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
  private List<ReviewEntity> reviewEntities;
}
