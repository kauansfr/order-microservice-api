package com.azenithsolutions.orderserviceapi.domain;

import java.util.List;
import java.util.Optional;

public interface OrderGateway {
    Order save(Order order);
    Optional<Order> findById(Long id);
    List<Order> findAll();
    void deleteById(Long id);
    boolean existsById(Long id);
}
