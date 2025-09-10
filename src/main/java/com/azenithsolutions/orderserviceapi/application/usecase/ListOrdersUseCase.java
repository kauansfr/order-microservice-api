package com.azenithsolutions.orderserviceapi.application.usecase;

import com.azenithsolutions.orderserviceapi.domain.Order;
import com.azenithsolutions.orderserviceapi.domain.OrderGateway;

import java.util.List;

public class ListOrdersUseCase {
    private final OrderGateway repository;

    public ListOrdersUseCase(OrderGateway repository) { this.repository = repository; }

    public List<Order> execute() {
        return repository.findAll();
    }
}
