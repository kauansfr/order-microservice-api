package com.azenithsolutions.orderserviceapi.infrastructure.persistence.repository;

import com.azenithsolutions.orderserviceapi.infrastructure.persistence.entity.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringDataOrderRepository extends JpaRepository<OrderEntity, Long> {
}
