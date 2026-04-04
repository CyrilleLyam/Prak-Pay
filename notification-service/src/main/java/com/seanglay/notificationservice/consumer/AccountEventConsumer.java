package com.seanglay.notificationservice.consumer;

import com.seanglay.events.AccountLoggedInEvent;
import com.seanglay.events.AccountRegisteredEvent;
import com.seanglay.notificationservice.mapper.NotificationMapper;
import com.seanglay.notificationservice.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class AccountEventConsumer {

    private final NotificationMapper notificationMapper;
    private final NotificationService notificationService;

    @KafkaListener(topics = "${spring.kafka.topic.registered}", groupId = "${spring.kafka.consumer.group-id}")
    public void onAccountRegistered(AccountRegisteredEvent event) {
        log.info("Received AccountRegisteredEvent: accountId={}, email={}", event.getAccountId(), event.getEmail());
        notificationService.create(notificationMapper.toNotification(event));
    }

    @KafkaListener(topics = "${spring.kafka.topic.logged-in}", groupId = "${spring.kafka.consumer.group-id}")
    public void onAccountLoggedIn(AccountLoggedInEvent event) {
        log.info("Received AccountLoggedInEvent: accountId={}, email={}", event.getAccountId(), event.getEmail());
        notificationService.create(notificationMapper.toNotification(event));
    }
}
