package com.seanglay.accountservice.controller;

import com.seanglay.accountservice.dto.AccountResponseDto;
import com.seanglay.accountservice.dto.CreateLoginRequestDto;
import com.seanglay.accountservice.dto.CreateRegisterRequestDto;
import com.seanglay.accountservice.dto.LoginResponseDto;
import com.seanglay.accountservice.service.AccountService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/accounts")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public AccountResponseDto register(@Valid @RequestBody CreateRegisterRequestDto dto) {
        return accountService.register(dto);
    }

    @PostMapping("/login")
    public LoginResponseDto login(@Valid @RequestBody CreateLoginRequestDto dto) {
        return accountService.login(dto);
    }
}
