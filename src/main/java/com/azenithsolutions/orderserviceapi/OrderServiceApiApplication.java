package com.azenithsolutions.orderserviceapi;

import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableRabbit
public class OrderServiceApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(OrderServiceApiApplication.class, args);
    }

}
