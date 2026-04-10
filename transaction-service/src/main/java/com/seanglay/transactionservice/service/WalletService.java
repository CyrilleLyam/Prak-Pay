package com.seanglay.transactionservice.service;

import com.seanglay.events.AccountRegisteredEvent;
import com.seanglay.transactionservice.event.WalletEventProducer;
import com.seanglay.transactionservice.mapper.WalletMapper;
import com.seanglay.transactionservice.model.Wallet;
import com.seanglay.transactionservice.repository.WalletRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class WalletService {
    private final WalletRepository walletRepository;
    private final WalletMapper walletMapper;
    private final WalletEventProducer walletEventProducer;

    public void create(AccountRegisteredEvent event) {
        Wallet wallet = walletMapper.toEntity(event);
        walletRepository.save(wallet);

        walletEventProducer.publishWalletCreated(walletMapper.toEvent(wallet));
    }
}
