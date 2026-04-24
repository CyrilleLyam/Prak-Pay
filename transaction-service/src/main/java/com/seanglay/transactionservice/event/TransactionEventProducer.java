package com.seanglay.transactionservice.event;

import com.seanglay.events.TransactionCreatedEvent;
import com.seanglay.kafka.publisher.KafkaEventPublisher;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TransactionEventProducer {

    @Value("${spring.kafka.topic.transaction-created}")
    private String transactionCreatedTopic;

    private final KafkaEventPublisher kafkaEventPublisher;

    public void publishTransactionCreated(TransactionCreatedEvent event) {
        kafkaEventPublisher.publish(transactionCreatedTopic, event.getWalletId(), event);
    }
}
