package org.project.resumeboost.reply.repository;

import java.util.List;

import org.project.resumeboost.reply.entity.ReplyEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReplyRepository extends JpaRepository<ReplyEntity, Long> {

  Page<ReplyEntity> findAllByBoardEntity_Id(Long id, Pageable pageable);

  List<ReplyEntity> findAllByBoardEntityId(Long id);

  Page<ReplyEntity> findAllByMemberEntityId(Long memberId, Pageable pageable);

}
