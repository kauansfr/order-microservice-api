package com.azenithsolutions.orderserviceapi.infrastructure.listener;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import com.azenithsolutions.orderserviceapi.application.usecase.CreateOrderUseCase;
import com.azenithsolutions.orderserviceapi.domain.Order;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CreateOrderListener {
    private final CreateOrderUseCase create;

    @RabbitListener(
        queues = "${app.messaging.order.queue}",
        containerFactory = "orderListenerContainerFactory"
    )
    public void onMessage(Order order) {
        try {
            System.out.println("Trying Create");
            create.execute(order);
            System.out.println("Order Created");
        } catch (Exception exception) {
            System.out.println("Error Creating Order");
            throw exception;
        }
    }
}
