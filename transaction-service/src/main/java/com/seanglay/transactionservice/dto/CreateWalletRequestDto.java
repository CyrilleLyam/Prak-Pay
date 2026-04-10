package com.seanglay.transactionservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateWalletRequestDto {
    private UUID accountId;
    @Builder.Default
    private BigDecimal balance = BigDecimal.ZERO;
}
