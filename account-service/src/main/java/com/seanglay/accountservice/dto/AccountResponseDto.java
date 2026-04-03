package com.seanglay.accountservice.dto;

import lombok.Builder;
import lombok.Data;

import java.time.Instant;

@Data
@Builder
public class AccountResponseDto {
    private String name;
    private String email;
    private Instant createdAt;
    private Instant updatedAt;
}
