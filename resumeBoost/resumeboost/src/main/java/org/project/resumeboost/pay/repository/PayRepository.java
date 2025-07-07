package org.project.resumeboost.pay.repository;

import java.util.List;

import org.project.resumeboost.pay.entity.PayEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PayRepository extends JpaRepository<PayEntity, Long> {

  List<PayEntity> findAllByMemberEntityId(Long id);

  Page<PayEntity> findByPaymentTypeContaining(Pageable pageable, String search);

  Page<PayEntity> findBypaymentTypeContaining(Pageable pageable, String search);

  Page<PayEntity> findAllByMemberEntityId(Pageable pageable, Long valueOf);

}
