package com.seanglay.notificationservice.consumer;

import com.seanglay.events.TransactionCreatedEvent;
import com.seanglay.notificationservice.mapper.NotificationMapper;
import com.seanglay.notificationservice.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class TransactionEventConsumer {

    private final NotificationMapper notificationMapper;
    private final NotificationService notificationService;

    @KafkaListener(topics = "${spring.kafka.topic.transaction-created}", groupId = "${spring.kafka.consumer.group-id}")
    public void onTransactionCreated(TransactionCreatedEvent event) {
        log.info("Received TransactionCreatedEvent: transactionId={}, accountId={}, type={}", event.getTransactionId(), event.getAccountId(), event.getType());
        notificationService.create(notificationMapper.toNotification(event));
    }
}
