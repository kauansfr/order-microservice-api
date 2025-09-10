package com.azenithsolutions.orderserviceapi.infrastructure.config;

import com.azenithsolutions.orderserviceapi.application.usecase.*;
import com.azenithsolutions.orderserviceapi.domain.OrderGateway;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OrderUseCaseConfig {

    @Bean
    CreateOrderUseCase createOrderUseCase(OrderGateway repository) { return new CreateOrderUseCase(repository); }

    @Bean
    UpdateOrderUseCase updateOrderUseCase(OrderGateway repository) { return new UpdateOrderUseCase(repository); }

    @Bean
    GetOrderByIdUseCase getOrderByIdUseCase(OrderGateway repository) { return new GetOrderByIdUseCase(repository); }

    @Bean
    ListOrdersUseCase listOrdersUseCase(OrderGateway repository) { return new ListOrdersUseCase(repository); }

    @Bean
    DeleteOrderUseCase deleteOrderUseCase(OrderGateway repository) { return new DeleteOrderUseCase(repository); }
}
