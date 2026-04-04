package com.seanglay.accountservice.config;

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

    @Value("${spring.kafka.topic.registered}")
    private String registeredTopic;

    @Value("${spring.kafka.topic.logged-in}")
    private String loggedInTopic;

    @Bean
    public NewTopic registeredTopic() {
        return TopicBuilder.name(registeredTopic).partitions(partitions).replicas(replicationFactor).build();
    }

    @Bean
    public NewTopic loggedInTopic() {
        return TopicBuilder.name(loggedInTopic).partitions(partitions).replicas(replicationFactor).build();
    }
}
