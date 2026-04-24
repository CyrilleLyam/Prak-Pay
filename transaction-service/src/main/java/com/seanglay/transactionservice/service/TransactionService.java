package com.seanglay.transactionservice.service;

import com.seanglay.common.exception.ResourceNotFoundException;
import com.seanglay.transactionservice.dto.CreateTransactionRequestDto;
import com.seanglay.transactionservice.dto.TransactionResponseDto;
import com.seanglay.transactionservice.event.TransactionEventProducer;
import com.seanglay.transactionservice.mapper.TransactionMapper;
import com.seanglay.transactionservice.model.Transaction;
import com.seanglay.transactionservice.model.TransactionType;
import com.seanglay.transactionservice.model.Wallet;
import com.seanglay.transactionservice.repository.TransactionRepository;
import com.seanglay.transactionservice.repository.WalletRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final WalletRepository walletRepository;
    private final TransactionMapper transactionMapper;
    private final TransactionEventProducer transactionEventProducer;

    @Transactional
    public TransactionResponseDto create(CreateTransactionRequestDto dto) {
        Wallet wallet = walletRepository.findById(dto.getWalletId()).orElseThrow(() -> new ResourceNotFoundException("Wallet not found: " + dto.getWalletId()));

        TransactionType type = dto.getType();

        if (type == TransactionType.TRANSFER_IN) {
            throw new IllegalArgumentException("Use TRANSFER_OUT to initiate a transfer");
        }

        Transaction transaction = switch (type) {
            case DEPOSIT -> handleDeposit(wallet, dto);
            case WITHDRAW -> handleWithdraw(wallet, dto);
            case TRANSFER_OUT -> handleTransfer(wallet, dto);
            default -> throw new IllegalArgumentException("Unsupported transaction type: " + type);
        };

        transactionEventProducer.publishTransactionCreated(transactionMapper.toEvent(transaction));

        return transactionMapper.toResponse(transaction);
    }

    private Transaction handleDeposit(Wallet wallet, CreateTransactionRequestDto dto) {
        wallet.setBalance(wallet.getBalance().add(dto.getAmount()));
        walletRepository.save(wallet);

        return transactionRepository.save(Transaction.builder().wallet(wallet).type(TransactionType.DEPOSIT).amount(dto.getAmount()).build());
    }

    private Transaction handleWithdraw(Wallet wallet, CreateTransactionRequestDto dto) {
        if (wallet.getBalance().compareTo(dto.getAmount()) < 0) {
            throw new IllegalStateException("Insufficient balance in wallet: " + wallet.getId());
        }

        wallet.setBalance(wallet.getBalance().subtract(dto.getAmount()));
        walletRepository.save(wallet);

        return transactionRepository.save(Transaction.builder().wallet(wallet).type(TransactionType.WITHDRAW).amount(dto.getAmount()).build());
    }

    private Transaction handleTransfer(Wallet sender, CreateTransactionRequestDto dto) {
        if (dto.getRelatedWalletId() == null) {
            throw new IllegalArgumentException("relatedWalletId is required for TRANSFER_OUT");
        }
        if (sender.getId().equals(dto.getRelatedWalletId())) {
            throw new IllegalArgumentException("Cannot transfer to the same wallet");
        }

        Wallet receiver = walletRepository.findById(dto.getRelatedWalletId()).orElseThrow(() -> new ResourceNotFoundException("Related wallet not found: " + dto.getRelatedWalletId()));

        if (sender.getBalance().compareTo(dto.getAmount()) < 0) {
            throw new IllegalStateException("Insufficient balance in wallet: " + sender.getId());
        }

        sender.setBalance(sender.getBalance().subtract(dto.getAmount()));
        receiver.setBalance(receiver.getBalance().add(dto.getAmount()));
        walletRepository.save(sender);
        walletRepository.save(receiver);

        Transaction outgoing = transactionRepository.save(Transaction.builder().wallet(sender).type(TransactionType.TRANSFER_OUT).amount(dto.getAmount()).relatedWallet(receiver).build());

        transactionRepository.save(Transaction.builder().wallet(receiver).type(TransactionType.TRANSFER_IN).amount(dto.getAmount()).relatedWallet(sender).build());

        return outgoing;
    }

}
