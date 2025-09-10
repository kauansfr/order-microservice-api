package com.azenithsolutions.orderserviceapi.web.order;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.azenithsolutions.orderserviceapi.application.usecase.CreateOrderUseCase;
import com.azenithsolutions.orderserviceapi.application.usecase.DeleteOrderUseCase;
import com.azenithsolutions.orderserviceapi.application.usecase.GetOrderByIdUseCase;
import com.azenithsolutions.orderserviceapi.application.usecase.ListOrdersUseCase;
import com.azenithsolutions.orderserviceapi.application.usecase.UpdateOrderUseCase;
import com.azenithsolutions.orderserviceapi.domain.Order;
import com.azenithsolutions.orderserviceapi.web.mappers.OrderRestMapper;
import com.azenithsolutions.orderserviceapi.web.rest.OrderRest;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {
    private final CreateOrderUseCase create;
    private final UpdateOrderUseCase update;
    private final GetOrderByIdUseCase getById;
    private final ListOrdersUseCase list;
    private final DeleteOrderUseCase delete;

    @GetMapping
    @Operation(summary = "List orders", description = "Returns all orders (v2 clean architecture)")
    public ResponseEntity<List<OrderRest>> getAll() {
        List<OrderRest> orders = list.execute().stream().map(OrderRestMapper::toRest).toList();
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get order by id", description = "Returns one order if it exists")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        try {
            OrderRest orderRest = OrderRestMapper.toRest(getById.execute(id));

            return ResponseEntity.ok(orderRest);
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PostMapping
    @Operation(summary = "Create order", description = "Creates a new order")
    public ResponseEntity<OrderRest> create(@Valid @RequestBody OrderRest orderRest) {
        System.out.println("Creating order");
        Order created = create.execute(OrderRestMapper.toDomain(orderRest));

        return ResponseEntity.status(HttpStatus.CREATED).body(OrderRestMapper.toRest(created));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update order", description = "Updates an existing order")
    public ResponseEntity<?> update(@PathVariable Long id, @Valid @RequestBody OrderRest orderRest) {
        try {
            Order updated = update.execute(id, OrderRestMapper.toDomain(orderRest));

            return ResponseEntity.ok(OrderRestMapper.toRest(updated));
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete order", description = "Deletes an order by id")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        try {
            delete.execute(id);
            return ResponseEntity.ok().build();
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}
