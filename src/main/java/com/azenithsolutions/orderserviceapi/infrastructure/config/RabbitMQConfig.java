package com.azenithsolutions.orderserviceapi.infrastructure.config;

import java.util.Map;

import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.ExchangeBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.rabbit.config.RetryInterceptorBuilder;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.RabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.retry.RepublishMessageRecoverer;
import org.springframework.amqp.support.converter.Jackson2JavaTypeMapper;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.interceptor.RetryOperationsInterceptor;

import com.fasterxml.jackson.databind.ObjectMapper;

@Configuration
public class RabbitMQConfig {
    @Value("${app.messaging.order.exchange}")
    private String orderExchangeName;
    @Value("${app.messaging.order.queue}")
    private String orderQueueName;
    @Value("${app.messaging.order.routing-key}")
    private String orderRoutingKey;
    @Value("${app.messaging.order.dlx}")
    private String orderDlxName;
    @Value("${app.messaging.order.dlq}")
    private String orderDlqName;

    @Bean
    public AmqpAdmin amqpAdmin(ConnectionFactory connectionFactory) {
        return new RabbitAdmin(connectionFactory);
    }

    @Bean
    public DirectExchange orderExchange() {
        return ExchangeBuilder
                .directExchange(orderExchangeName)
                .durable(true)
                .build();
    }

    @Bean
    public DirectExchange orderDeadLetterExchange() {
        return ExchangeBuilder
                .directExchange(orderDlxName)
                .durable(true)
                .build();
    }

    @Bean
    public Queue orderQueue() {
        return QueueBuilder.durable(orderQueueName)
                .withArguments(Map.of(
                        "x-dead-letter-exchange", orderDlxName,
                        "x-dead-letter-routing-key", orderRoutingKey // (mesmo routing-key para simplicidade)
                ))
                .build();
    }

    @Bean
    public Queue orderDeadLetterQueue() {
        return QueueBuilder.durable(orderDlqName).build();
    }

    @Bean
    public Binding orderBinding(Queue orderQueue, DirectExchange orderExchange) {
        return BindingBuilder
                .bind(orderQueue)
                .to(orderExchange)
                .with(orderRoutingKey);
    }

    @Bean
    public Binding orderDlqBinding(Queue orderDeadLetterQueue, DirectExchange orderDeadLetterExchange) {
        return BindingBuilder
                .bind(orderDeadLetterQueue)
                .to(orderDeadLetterExchange)
                .with(orderRoutingKey);
    }

    @Bean
    public MessageConverter jacksonMessageConverter(ObjectMapper objectMapper) {
        Jackson2JsonMessageConverter converter = new Jackson2JsonMessageConverter(objectMapper);
        converter.setTypePrecedence(Jackson2JavaTypeMapper.TypePrecedence.INFERRED);
        return converter;
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory, MessageConverter jacksonMessageConverter) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(jacksonMessageConverter);
        return template;
    }

    @Bean
    public RetryOperationsInterceptor orderRetryInterceptor(RabbitTemplate rabbitTemplate) {
        RepublishMessageRecoverer recoverer = new RepublishMessageRecoverer(
                rabbitTemplate,
                orderDlxName,
                orderRoutingKey
        );

        return RetryInterceptorBuilder.stateless()
                .maxAttempts(3)
                .backOffOptions(1000, 2.0, 10000) // initial 1s, multiplica por 2 - até 10s
                .recoverer(recoverer)
                .build();
    }

    @Bean
    public RabbitListenerContainerFactory<?> orderListenerContainerFactory(
            ConnectionFactory connectionFactory,
            MessageConverter jacksonMessageConverter,
            RetryOperationsInterceptor orderRetryInterceptor) {

        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setConcurrentConsumers(2);
        factory.setMaxConcurrentConsumers(5);
        factory.setDefaultRequeueRejected(false);
        factory.setMessageConverter(jacksonMessageConverter);
        factory.setAdviceChain(orderRetryInterceptor);
        return factory;
    }
}
