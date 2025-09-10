package com.azenithsolutions.orderserviceapi.application.usecase;

import com.azenithsolutions.orderserviceapi.domain.OrderGateway;

import java.util.NoSuchElementException;

public class DeleteOrderUseCase {
    private final OrderGateway repository;

    public DeleteOrderUseCase(OrderGateway repository) { this.repository = repository; }

    public void execute(Long id) {
        if (!repository.existsById(id)) {
            throw new NoSuchElementException("Pedido não encontrado!");
        }
        repository.deleteById(id);
    }
}
