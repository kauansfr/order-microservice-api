package com.azenithsolutions.orderserviceapi.application.usecase;

import com.azenithsolutions.orderserviceapi.domain.Order;
import com.azenithsolutions.orderserviceapi.domain.OrderGateway;

public class CreateOrderUseCase {
    private final OrderGateway repository;

    public CreateOrderUseCase(OrderGateway repository) { this.repository = repository; }

    public Order execute(Order order) {
        System.out.println("Creating orderUSECAS");
        return repository.save(order);
    }
}
