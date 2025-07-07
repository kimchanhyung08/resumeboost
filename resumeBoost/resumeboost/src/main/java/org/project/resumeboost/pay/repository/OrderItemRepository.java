package org.project.resumeboost.pay.repository;

import org.project.resumeboost.pay.entity.OrderItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItemEntity,Long>{
  
}
