package com.simulator.exchange.config;
//This file does Topic configuration for Kafka.
//There will be two topics 1.order-topic is used to place buy/sell order
//price-topic is used to get and update current price of stock
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopicConfig {

    @Bean
    public NewTopic ordersTopic() {
        return TopicBuilder.name("orders-topic")
                .partitions(1)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic priceTopic() {
        return TopicBuilder.name("price-topic")
                .partitions(1)
                .replicas(1)
                .build();
    }
}
