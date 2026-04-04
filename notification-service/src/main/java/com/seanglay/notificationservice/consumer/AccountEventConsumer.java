package com.seanglay.notificationservice.consumer;

import com.seanglay.events.AccountLoggedInEvent;
import com.seanglay.events.AccountRegisteredEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class AccountEventConsumer {

    @KafkaListener(topics = "${spring.kafka.topic.registered}", groupId = "${spring.kafka.consumer.group-id}")
    public void onAccountRegistered(AccountRegisteredEvent event) {
        log.info("Received AccountRegisteredEvent: accountId={}, email={}", event.getAccountId(), event.getEmail());
    }

    @KafkaListener(topics = "${spring.kafka.topic.logged-in}", groupId = "${spring.kafka.consumer.group-id}")
    public void onAccountLoggedIn(AccountLoggedInEvent event) {
        log.info("Received AccountLoggedInEvent: accountId={}, email={}", event.getAccountId(), event.getEmail());
    }
}
