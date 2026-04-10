package com.seanglay.transactionservice.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaConfig {
    @Value("${spring.kafka.topic.partitions:1}")
    private int partitions;

    @Value("${spring.kafka.topic.replication-factor:1}")
    private short replicationFactor;

    @Value("${spring.kafka.topic.wallet-created}")
    private String walletCreatedTopic;

    @Bean
    public NewTopic registeredTopic() {
        return TopicBuilder.name(walletCreatedTopic).partitions(partitions).replicas(replicationFactor).build();
    }

}
