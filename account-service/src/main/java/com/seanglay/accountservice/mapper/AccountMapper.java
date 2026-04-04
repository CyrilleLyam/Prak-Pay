package com.seanglay.accountservice.mapper;

import com.seanglay.accountservice.dto.AccountResponseDto;
import com.seanglay.accountservice.dto.CreateRegisterRequestDto;
import com.seanglay.accountservice.model.Account;
import com.seanglay.events.AccountLoggedInEvent;
import com.seanglay.events.AccountRegisteredEvent;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AccountMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Account toEntity(CreateRegisterRequestDto dto);

    AccountResponseDto toResponse(Account account);

    @Mapping(target = "accountId", expression = "java(account.getId().toString())")
    @Mapping(target = "registeredAt", source = "createdAt")
    AccountRegisteredEvent toRegisteredEvent(Account account);

    @Mapping(target = "accountId", expression = "java(account.getId().toString())")
    @Mapping(target = "loggedInAt", expression = "java(java.time.Instant.now())")
    AccountLoggedInEvent toLoggedInEvent(Account account);
}
