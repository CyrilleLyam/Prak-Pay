package com.seanglay.transactionservice.event;

import com.seanglay.events.WalletCreatedEvent;
import com.seanglay.kafka.publisher.KafkaEventPublisher;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class WalletEventProducer {
    @Value("${spring.kafka.topic.wallet-created}")
    private String walletCreatedTopic;

    private final KafkaEventPublisher kafkaEventPublisher;

    public void publishWalletCreated(WalletCreatedEvent event) {
        kafkaEventPublisher.publish(walletCreatedTopic, event.getAccountId(), event);
    }
}
