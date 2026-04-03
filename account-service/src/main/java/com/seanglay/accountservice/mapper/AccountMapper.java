package com.seanglay.accountservice.mapper;

import com.seanglay.accountservice.dto.AccountResponseDto;
import com.seanglay.accountservice.dto.CreateRegisterRequestDto;
import com.seanglay.accountservice.model.Account;
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
}
