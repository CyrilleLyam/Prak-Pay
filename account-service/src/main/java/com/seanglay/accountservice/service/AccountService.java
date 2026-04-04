package com.seanglay.accountservice.service;

import com.seanglay.accountservice.dto.AccountResponseDto;
import com.seanglay.accountservice.dto.CreateLoginRequestDto;
import com.seanglay.accountservice.dto.CreateRegisterRequestDto;
import com.seanglay.accountservice.dto.LoginResponseDto;
import com.seanglay.accountservice.event.AccountEventProducer;
import com.seanglay.accountservice.mapper.AccountMapper;
import com.seanglay.accountservice.model.Account;
import com.seanglay.accountservice.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;
    private final AccountMapper accountMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AccountEventProducer accountEventProducer;

    public AccountResponseDto register(CreateRegisterRequestDto dto) {
        Account account = accountMapper.toEntity(dto);
        account.setPassword(passwordEncoder.encode(dto.getPassword()));
        Account saved = accountRepository.save(account);

        accountEventProducer.publishRegistered(accountMapper.toRegisteredEvent(saved));

        return accountMapper.toResponse(saved);
    }

    public LoginResponseDto login(CreateLoginRequestDto dto) {
        Account account = accountRepository.findByEmail(dto.getEmail()).orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials"));

        if (!passwordEncoder.matches(dto.getPassword(), account.getPassword())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials");
        }

        accountEventProducer.publishLoggedIn(accountMapper.toLoggedInEvent(account));

        return LoginResponseDto.builder().accessToken(jwtService.generateAccessToken(account.getId(), account.getEmail())).accessTokenExpiredAt(jwtService.getAccessTokenExpiry()).build();
    }
}
