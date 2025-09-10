package com.azenithsolutions.orderserviceapi.infrastructure.persistence.adapter;

import com.azenithsolutions.orderserviceapi.domain.Order;
import com.azenithsolutions.orderserviceapi.domain.OrderGateway;
import com.azenithsolutions.orderserviceapi.infrastructure.persistence.entity.OrderEntity;
import com.azenithsolutions.orderserviceapi.infrastructure.persistence.repository.SpringDataOrderRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OrderGatewayAdapter implements OrderGateway {
    private final SpringDataOrderRepository repository;

    public OrderGatewayAdapter(SpringDataOrderRepository repository) { this.repository = repository; }

    @Override
    public Order save(Order order) {
        System.out.println("Trying CreateREPO");
        OrderEntity entity = toEntity(order);
        OrderEntity saved = repository.save(entity);
        return toDomain(saved);
    }

    @Override
    public Optional<Order> findById(Long id) { return repository.findById(id).map(this::toDomain); }

    @Override
    public List<Order> findAll() { return repository.findAll().stream().map(this::toDomain).toList(); }

    @Override
    public void deleteById(Long id) { repository.deleteById(id); }

    @Override
    public boolean existsById(Long id) { return repository.existsById(id); }

    private OrderEntity toEntity(Order order) {
        OrderEntity entity = new OrderEntity();
        entity.setId(order.getId());
        entity.setCodigo(order.getCodigo());
        entity.setNomeComprador(order.getNomeComprador());
        entity.setEmailComprador(order.getEmailComprador());
        entity.setCnpj(order.getCnpj());
        entity.setValor(order.getValor());
        entity.setStatus(order.getStatus());
        entity.setTelCelular(order.getTelCelular());
        entity.setCreatedAt(order.getCreatedAt());
        entity.setUpdatedAt(order.getUpdatedAt());
        return entity;
    }

    private Order toDomain(OrderEntity entity) {
        Order domain  = new Order();
        domain.setId(entity.getId());
        domain.setCodigo(entity.getCodigo());
        domain.setNomeComprador(entity.getNomeComprador());
        domain.setEmailComprador(entity.getEmailComprador());
        domain.setCnpj(entity.getCnpj());
        domain.setValor(entity.getValor());
        domain.setStatus(entity.getStatus());
        domain.setTelCelular(entity.getTelCelular());
        domain.setCreatedAt(entity.getCreatedAt());
        domain.setUpdatedAt(entity.getUpdatedAt());
        return domain;
    }
}
