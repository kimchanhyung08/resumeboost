package org.project.resumeboost.event.repository;

import java.util.List;

import org.project.resumeboost.event.entity.EventEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventRepository extends JpaRepository<EventEntity, Long> {

  List<EventEntity> findAllByMemberEntityId(Long memberId);
  
}
