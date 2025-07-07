package org.project.resumeboost.member.dto;

import java.util.List;

import org.project.resumeboost.basic.common.Role;
import org.project.resumeboost.board.entity.BoardEntity;
import org.project.resumeboost.item.entity.ItemEntity;
import org.project.resumeboost.member.entity.MemberEntity;
import org.project.resumeboost.member.entity.MemberImgEntity;
import org.project.resumeboost.member.entity.MemberPtEntity;
import org.project.resumeboost.pay.entity.PayEntity;
import org.project.resumeboost.reply.entity.ReplyEntity;
import org.project.resumeboost.review.entity.ReviewEntity;
import org.springframework.web.multipart.MultipartFile;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class MemberDto {
  private Long id;

  private String userEmail;

  private String userPw;

  // 나이
  private int age;

  // 지역
  private String address;

  // 별명
  private String nickName;

  // 실제 이름
  private String userName;

  // 상세설명
  private String detail;

  // 리뷰수
  private int replyCount;

  private int viewCount;

  // 내가 작성한 댓글 수
  private int myReplyCount;

  // 내가 작성한 게시글 수
  private int myPostCount;

  // 경력
  private String career;

  // 권한 MEMBER(일반회원), MENTOR(멘토회원), ADMIN
  private Role role;

  // 프로필 사진 유무
  private int attachFile;

  // 포트폴리오 유무
  private int portfolioFile;

  // 전화번호
  private String phone;

  private MultipartFile profileFile;

  private MultipartFile ptFile;

  // 소셜로그인
  private Boolean social;

  // 게시판
  private List<BoardEntity> boardEntities;

  // 리뷰
  private List<ReplyEntity> replyEntities;

  // 상품
  private List<ItemEntity> itemEntities;

  // 결제
  private List<PayEntity> payEntities;

  // 프로필사진 파일
  private List<MemberImgEntity> memberImgEntities;

  private String newImgName;

  private String oldImgName;

  // 폴트폴리오 파일
  private List<MemberPtEntity> memberPtEntities;

  private String newPtName;

  private String oldPtName;

  private List<ReviewEntity> reviewEntities;

  public static MemberDto toMemberDto(MemberEntity memberEntity) {
    if (memberEntity.getAttachFile() == 1 && memberEntity.getPortfolioFile() == 1) {
      return MemberDto.builder()
          .id(memberEntity.getId())
          .address(memberEntity.getAddress())
          .age(memberEntity.getAge())
          .attachFile(memberEntity.getAttachFile())
          .career(memberEntity.getCareer())
          .detail(memberEntity.getDetail())
          .itemEntities(memberEntity.getItemEntities())
          .memberImgEntities(memberEntity.getMemberImgEntities())
          .newImgName(memberEntity.getMemberImgEntities().get(0).getNewImgName())
          .oldImgName(memberEntity.getMemberImgEntities().get(0).getOldImgName())
          .portfolioFile(memberEntity.getPortfolioFile())
          .memberPtEntities(memberEntity.getMemberPtEntities())
          .newPtName(memberEntity.getMemberPtEntities().get(0).getNewPtName())
          .oldPtName(memberEntity.getMemberPtEntities().get(0).getOldPtName())
          .myPostCount(memberEntity.getMyPostCount())
          .myReplyCount(memberEntity.getMyReplyCount())
          .nickName(memberEntity.getNickName())
          .payEntities(memberEntity.getPayEntities())
          .phone(memberEntity.getPhone())
          .replyCount(memberEntity.getReplyCount())
          .replyEntities(memberEntity.getReplyEntities())
          .role(memberEntity.getRole())
          .userEmail(memberEntity.getUserEmail())
          .userName(memberEntity.getUserName())
          .viewCount(memberEntity.getViewCount())
          .social(memberEntity.getSocial())
          .userPw(memberEntity.getUserPw())
          .boardEntities(memberEntity.getBoardEntities())
          .reviewEntities(memberEntity.getReviewEntities())
          .build();
    } else if (memberEntity.getAttachFile() == 1) {
      return MemberDto.builder()
          .id(memberEntity.getId())
          .address(memberEntity.getAddress())
          .age(memberEntity.getAge())
          .attachFile(memberEntity.getAttachFile())
          .portfolioFile(memberEntity.getPortfolioFile())
          .career(memberEntity.getCareer())
          .detail(memberEntity.getDetail())
          .itemEntities(memberEntity.getItemEntities())
          .memberImgEntities(memberEntity.getMemberImgEntities())
          .newImgName(memberEntity.getMemberImgEntities().get(0).getNewImgName())
          .oldImgName(memberEntity.getMemberImgEntities().get(0).getOldImgName())
          .myPostCount(memberEntity.getMyPostCount())
          .myReplyCount(memberEntity.getMyReplyCount())
          .nickName(memberEntity.getNickName())
          .payEntities(memberEntity.getPayEntities())
          .phone(memberEntity.getPhone())
          .replyCount(memberEntity.getReplyCount())
          .replyEntities(memberEntity.getReplyEntities())
          .role(memberEntity.getRole())
          .userEmail(memberEntity.getUserEmail())
          .userName(memberEntity.getUserName())
          .viewCount(memberEntity.getViewCount())
          .social(memberEntity.getSocial())
          .userPw(memberEntity.getUserPw())
          .boardEntities(memberEntity.getBoardEntities())
          .reviewEntities(memberEntity.getReviewEntities())
          .build();
    } else if (memberEntity.getPortfolioFile() == 1) {
      return MemberDto.builder()
          .id(memberEntity.getId())
          .address(memberEntity.getAddress())
          .age(memberEntity.getAge())
          .attachFile(memberEntity.getAttachFile())
          .career(memberEntity.getCareer())
          .detail(memberEntity.getDetail())
          .itemEntities(memberEntity.getItemEntities())
          .portfolioFile(memberEntity.getPortfolioFile())
          .memberPtEntities(memberEntity.getMemberPtEntities())
          .newPtName(memberEntity.getMemberPtEntities().get(0).getNewPtName())
          .oldPtName(memberEntity.getMemberPtEntities().get(0).getOldPtName())
          .myPostCount(memberEntity.getMyPostCount())
          .myReplyCount(memberEntity.getMyReplyCount())
          .nickName(memberEntity.getNickName())
          .payEntities(memberEntity.getPayEntities())
          .phone(memberEntity.getPhone())
          .replyCount(memberEntity.getReplyCount())
          .replyEntities(memberEntity.getReplyEntities())
          .role(memberEntity.getRole())
          .userEmail(memberEntity.getUserEmail())
          .userName(memberEntity.getUserName())
          .viewCount(memberEntity.getViewCount())
          .social(memberEntity.getSocial())
          .userPw(memberEntity.getUserPw())
          .boardEntities(memberEntity.getBoardEntities())
          .reviewEntities(memberEntity.getReviewEntities())
          .build();
    } else {
      return MemberDto.builder()
          .id(memberEntity.getId())
          .address(memberEntity.getAddress())
          .age(memberEntity.getAge())
          .attachFile(memberEntity.getAttachFile())
          .career(memberEntity.getCareer())
          .detail(memberEntity.getDetail())
          .itemEntities(memberEntity.getItemEntities())
          .portfolioFile(memberEntity.getPortfolioFile())
          .myPostCount(memberEntity.getMyPostCount())
          .myReplyCount(memberEntity.getMyReplyCount())
          .nickName(memberEntity.getNickName())
          .payEntities(memberEntity.getPayEntities())
          .phone(memberEntity.getPhone())
          .replyCount(memberEntity.getReplyCount())
          .replyEntities(memberEntity.getReplyEntities())
          .role(memberEntity.getRole())
          .userEmail(memberEntity.getUserEmail())
          .userName(memberEntity.getUserName())
          .viewCount(memberEntity.getViewCount())
          .social(memberEntity.getSocial())
          .userPw(memberEntity.getUserPw())
          .boardEntities(memberEntity.getBoardEntities())
          .reviewEntities(memberEntity.getReviewEntities())
          .build();
    }
  }
}
