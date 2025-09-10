package com.azenithsolutions.orderserviceapi.application.usecase;

import com.azenithsolutions.orderserviceapi.domain.Order;
import com.azenithsolutions.orderserviceapi.domain.OrderGateway;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;

public class UpdateOrderUseCase {
    private final OrderGateway repository;

    public UpdateOrderUseCase(OrderGateway repository) {
        this.repository = repository;
    }

    public Order execute(Long id, Order newOrder) {
        Order existing = repository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Pedido não encontrado!"));

        existing.setCodigo(newOrder.getCodigo());
        existing.setNomeComprador(newOrder.getNomeComprador());
        existing.setEmailComprador(newOrder.getEmailComprador());
        existing.setCnpj(newOrder.getCnpj());
        existing.setValor(newOrder.getValor());
        existing.setStatus(newOrder.getStatus());
        existing.setTelCelular(newOrder.getTelCelular());
        existing.setUpdatedAt(LocalDateTime.now());

        return repository.save(existing);
    }
}
