package org.project.resumeboost.cart.repository;

import java.util.Optional;

import org.project.resumeboost.cart.entity.CartEntity;
import org.project.resumeboost.member.entity.MemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<CartEntity,Long>{

  Optional<CartEntity> findByMemberEntity(MemberEntity memberEntity);

  Optional<CartEntity> findByMemberEntityId(Long id);
  
}
