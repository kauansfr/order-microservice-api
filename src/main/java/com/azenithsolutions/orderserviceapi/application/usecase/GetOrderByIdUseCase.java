package com.azenithsolutions.orderserviceapi.application.usecase;

import com.azenithsolutions.orderserviceapi.domain.Order;
import com.azenithsolutions.orderserviceapi.domain.OrderGateway;

import java.util.NoSuchElementException;

public class GetOrderByIdUseCase {
    private final OrderGateway repository;

    public GetOrderByIdUseCase(OrderGateway repository) { this.repository = repository; }

    public Order execute(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Pedido não encontrado!"));
    }
}
