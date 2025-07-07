package org.project.resumeboost.item.repository;

import java.util.List;

import org.project.resumeboost.item.entity.ItemEntity;
import org.project.resumeboost.member.entity.MemberEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemRepository extends JpaRepository<ItemEntity, Long> {

  List<ItemEntity> findAllByMemberEntity(MemberEntity memberEntity);

  Page<ItemEntity> findByCategoryContaining(Pageable pageable, String category);

  Page<ItemEntity> findByMemberEntity(Pageable pageable, MemberEntity memberEntity);

  List<ItemEntity> findByMemberEntity(MemberEntity mentor);

  List<ItemEntity> findByMemberEntityId(Long mentorId);

}
