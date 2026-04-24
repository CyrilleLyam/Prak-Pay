package com.seanglay.transactionservice.mapper;

import com.seanglay.events.TransactionCreatedEvent;
import com.seanglay.transactionservice.dto.TransactionResponseDto;
import com.seanglay.transactionservice.model.Transaction;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.math.BigDecimal;
import java.nio.ByteBuffer;

@Mapper(componentModel = "spring")
public interface TransactionMapper {

    @Mapping(source = "wallet.id", target = "walletId")
    @Mapping(source = "relatedWallet.id", target = "relatedWalletId")
    TransactionResponseDto toResponse(Transaction transaction);

    @Mapping(target = "transactionId", expression = "java(transaction.getId().toString())")
    @Mapping(target = "accountId", expression = "java(transaction.getWallet().getAccountId().toString())")
    @Mapping(target = "walletId", expression = "java(transaction.getWallet().getId().toString())")
    @Mapping(target = "relatedWalletId", expression = "java(transaction.getRelatedWallet() != null ? transaction.getRelatedWallet().getId().toString() : null)")
    @Mapping(target = "createdAt", expression = "java(transaction.getCreatedAt())")
    TransactionCreatedEvent toEvent(Transaction transaction);

    default ByteBuffer map(BigDecimal value) {
        return ByteBuffer.wrap(value.unscaledValue().toByteArray());
    }
}
