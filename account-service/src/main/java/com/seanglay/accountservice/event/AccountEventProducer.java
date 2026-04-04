package com.seanglay.accountservice.event;

import com.seanglay.events.AccountLoggedInEvent;
import com.seanglay.events.AccountRegisteredEvent;
import com.seanglay.kafka.publisher.KafkaEventPublisher;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AccountEventProducer {

    @Value("${spring.kafka.topic.registered}")
    private String topicRegistered;

    @Value("${spring.kafka.topic.logged-in}")
    private String topicLoggedIn;

    private final KafkaEventPublisher kafkaEventPublisher;

    public void publishRegistered(AccountRegisteredEvent event) {
        kafkaEventPublisher.publish(topicRegistered, event.getAccountId(), event);
    }

    public void publishLoggedIn(AccountLoggedInEvent event) {
        kafkaEventPublisher.publish(topicLoggedIn, event.getAccountId(), event);
    }
}
