package org.project.resumeboost.member.repository;

import java.util.Optional;

import org.project.resumeboost.member.entity.MemberEntity;
import org.project.resumeboost.member.entity.MemberImgEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberImgRepository extends JpaRepository<MemberImgEntity, Long> {

  Optional<MemberImgEntity> findByMemberEntity(MemberEntity memberEntity);

}
