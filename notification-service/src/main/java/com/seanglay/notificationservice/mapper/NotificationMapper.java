package com.seanglay.notificationservice.mapper;

import com.seanglay.events.AccountLoggedInEvent;
import com.seanglay.events.AccountRegisteredEvent;
import com.seanglay.events.WalletCreatedEvent;
import com.seanglay.notificationservice.model.Notification;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.HashMap;
import java.util.Map;

@Mapper(componentModel = "spring")
public interface NotificationMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "amount", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "accountId", expression = "java(java.util.UUID.fromString(event.getAccountId()))")
    @Mapping(target = "type", constant = "ACCOUNT_REGISTERED")
    @Mapping(target = "message", expression = "java(\"Welcome, \" + event.getName() + \"! Your account has been successfully registered.\")")
    @Mapping(target = "metadata", expression = "java(metadata(\"registeredAt\", event.getRegisteredAt().toString()))")
    Notification toNotification(AccountRegisteredEvent event);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "amount", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "accountId", expression = "java(java.util.UUID.fromString(event.getAccountId()))")
    @Mapping(target = "type", constant = "ACCOUNT_LOGGED_IN")
    @Mapping(target = "message", constant = "You have successfully logged in to your account.")
    @Mapping(target = "metadata", expression = "java(metadata(\"loggedInAt\", event.getLoggedInAt().toString()))")
    Notification toNotification(AccountLoggedInEvent event);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "email", ignore = true)
    @Mapping(target = "amount", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "accountId", expression = "java(java.util.UUID.fromString(event.getAccountId()))")
    @Mapping(target = "type", constant = "WALLET_CREATED")
    @Mapping(target = "message", expression = "java(\"Your wallet has been successfully created.\")")
    @Mapping(target = "metadata", expression = "java(metadata(\"walletId\", event.getWalletId()))")
    Notification toNotification(WalletCreatedEvent event);

    default Map<String, Object> metadata(String key, Object value) {
        Map<String, Object> map = new HashMap<>();
        map.put(key, value);
        return map;
    }
}
