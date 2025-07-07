package org.project.resumeboost.member.repository;

import java.util.Optional;

import org.project.resumeboost.member.entity.MemberEntity;
import org.project.resumeboost.member.entity.MemberPtEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberPtRepository extends JpaRepository<MemberPtEntity, Long> {

  Optional<MemberPtEntity> findByMemberEntity(MemberEntity memberEntity);

}
