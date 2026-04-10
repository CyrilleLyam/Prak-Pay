package com.seanglay.notificationservice.consumer;

import com.seanglay.events.WalletCreatedEvent;
import com.seanglay.notificationservice.mapper.NotificationMapper;
import com.seanglay.notificationservice.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class WalletEventConsumer {
    private final NotificationMapper notificationMapper;
    private final NotificationService notificationService;

    @KafkaListener(topics = "${spring.kafka.topic.wallet-created}", groupId = "${spring.kafka.consumer.group-id}")
    public void onWalletCreated(WalletCreatedEvent event) {
        log.info("Received WalletCreatedEvent: accountId={}, walletId={}", event.getAccountId(), event.getWalletId());
        notificationService.create(notificationMapper.toNotification(event));
    }
}
