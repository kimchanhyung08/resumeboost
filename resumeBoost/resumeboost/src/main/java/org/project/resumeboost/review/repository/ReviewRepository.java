package org.project.resumeboost.review.repository;

import java.util.List;

import org.project.resumeboost.review.entity.ReviewEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewRepository extends JpaRepository<ReviewEntity, Long> {

  List<ReviewEntity> findAllByMentorId(Long mentorId);

  List<ReviewEntity> findByMentorId(Long mentorId);

  List<ReviewEntity> findByMemberEntityId(Long memberId);

}
