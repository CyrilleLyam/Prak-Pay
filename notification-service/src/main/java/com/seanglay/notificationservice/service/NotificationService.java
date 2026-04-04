package com.seanglay.notificationservice.service;

import com.seanglay.notificationservice.model.Notification;
import com.seanglay.notificationservice.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;

    public void create(Notification notification) {
        notificationRepository.save(notification);
        log.info("Notification saved: accountId={}, type={}", notification.getAccountId(), notification.getType());
    }
}
