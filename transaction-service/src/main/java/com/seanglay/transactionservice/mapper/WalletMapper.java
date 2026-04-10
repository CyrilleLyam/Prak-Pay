package com.seanglay.transactionservice.mapper;

import com.seanglay.events.AccountRegisteredEvent;
import com.seanglay.events.WalletCreatedEvent;
import com.seanglay.transactionservice.dto.CreateWalletRequestDto;
import com.seanglay.transactionservice.model.Wallet;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface WalletMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "deletedAt", ignore = true)
    @Mapping(target = "transactions", ignore = true)
    Wallet toEntity(CreateWalletRequestDto dto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "accountId", expression = "java(java.util.UUID.fromString(event.getAccountId()))")
    @Mapping(target = "balance", expression = "java(java.math.BigDecimal.ZERO)")
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "deletedAt", ignore = true)
    @Mapping(target = "transactions", ignore = true)
    Wallet toEntity(AccountRegisteredEvent event);

    @Mapping(target = "walletId", expression = "java(wallet.getId().toString())")
    @Mapping(target = "accountId", expression = "java(wallet.getAccountId().toString())")
    @Mapping(target = "createdAt", expression = "java(java.time.Instant.now())")
    WalletCreatedEvent toEvent(Wallet wallet);
}
