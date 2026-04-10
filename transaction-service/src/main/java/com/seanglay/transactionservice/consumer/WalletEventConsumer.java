package com.seanglay.transactionservice.consumer;

import com.seanglay.events.AccountRegisteredEvent;
import com.seanglay.transactionservice.service.WalletService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class WalletEventConsumer {
    private final WalletService walletService;

    @KafkaListener(topics = "${spring.kafka.topic.registered}", groupId = "${spring.kafka.consumer.group-id}")
    public void onAccountRegistered(AccountRegisteredEvent event) {
        log.info("Received AccountRegisteredEvent: accountId={}, email={}", event.getAccountId(), event.getEmail());
        walletService.create(event);
    }
}
