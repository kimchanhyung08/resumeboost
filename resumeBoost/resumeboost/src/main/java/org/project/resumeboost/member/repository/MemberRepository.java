package org.project.resumeboost.member.repository;

import java.util.List;
import java.util.Optional;

import org.project.resumeboost.basic.common.Role;
import org.project.resumeboost.member.entity.MemberEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends JpaRepository<MemberEntity, Long> {

  Optional<MemberEntity> findByUserEmail(String userEmail);

  Optional<MemberEntity> findByNickName(String nickName);

  // 멘토 조회?��
  @Modifying // JPQL
  @Query("update MemberEntity m set m.viewCount = m.viewCount + 1 where m.id = :id")
  void MentorViewCount(@Param("id") Long id);

  @Modifying // JPQL
  @Query("update MemberEntity m set m.myPostCount = m.myPostCount + 1 where m.id = :id")
  void myPostCount(@Param("id") Long memberId);

  @Modifying // JPQL
  @Query("update MemberEntity m set m.myPostCount = m.myPostCount - 1 where m.id = :id")
  void myPostCountDelete(@Param("id") Long id);

  @Modifying // JPQL
  @Query("update MemberEntity m set m.myReplyCount = m.myReplyCount + 1 where m.id = :id")
  void myReplyCount(@Param("id") Long memberId);

  @Modifying // JPQL
  @Query("update MemberEntity m set m.myReplyCount = m.myReplyCount - 1 where m.id = :id")
  void myReplyCountDelete(@Param("id") Long id);

  Page<MemberEntity> findByNickNameContaining(Pageable pageable, String search);

  Page<MemberEntity> findByAddressContaining(Pageable pageable, String search);

  Page<MemberEntity> findByRole(Pageable pageable, Role mentor);

  Page<MemberEntity> findByRoleAndNickNameContaining(Pageable pageable, Role mentor, String search);

  Page<MemberEntity> findByRoleAndAddressContaining(Pageable pageable, Role mentor, String search);

  List<MemberEntity> findByRole(Role mentor);

  // @Modifying
  // @Query("SELECT m FROM MemberEntity m WHERE m.role = :role ORDER BY
  // m.replyCount DESC")
  Page<MemberEntity> findByRoleOrderByReplyCountDesc(Role role, Pageable pageable);

  // @Modifying
  // @Query("SELECT m FROM MemberEntity m WHERE m.role = :role ORDER BY
  // m.viewCount DESC")
  Page<MemberEntity> findByRoleOrderByViewCountDesc(Role role, Pageable pageable);

  Page<MemberEntity> findAllByUserNameContaining(Pageable pageable, String search);

  Page<MemberEntity> findAllByNickNameContaining(Pageable pageable, String search);

  Page<MemberEntity> findById(Pageable pageable, Long valueOf);

}