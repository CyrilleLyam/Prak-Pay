package com.seanglay.transactionservice.dto;

import com.seanglay.transactionservice.model.TransactionType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransactionResponseDto {
    private UUID id;
    private UUID walletId;
    private TransactionType type;
    private BigDecimal amount;
    private UUID relatedWalletId;
    private Instant createdAt;
}
