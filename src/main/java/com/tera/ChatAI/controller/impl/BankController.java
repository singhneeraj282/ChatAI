package com.tera.ChatAI.controller.impl;

import com.tera.ChatAI.controller.BankAPI;
import com.tera.ChatAI.dto.AccountDTO;
import com.tera.ChatAI.dto.CustomerDTO;
import com.tera.ChatAI.dto.TransactionDTO;
import com.tera.ChatAI.mapper.dto.AccountDTOMapper;
import com.tera.ChatAI.mapper.dto.CustomerDTOMapper;
import com.tera.ChatAI.mapper.dto.TransactionDTOMapper;
import com.tera.ChatAI.service.BankService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class BankController implements BankAPI {

    private final BankService bankService;
    private final AccountDTOMapper accountDTOMapper;
    private final CustomerDTOMapper customerDTOMapper;
    private final TransactionDTOMapper transactionDTOMapper;


    @Override
    public ResponseEntity<List<CustomerDTO>> findAllCustomers() {
        return ResponseEntity.ok(customerDTOMapper.map(bankService.findAllCustomers()));
    }

    @Override
    public ResponseEntity<CustomerDTO> findCustomerById(Long customerId) {
        return ResponseEntity.ok(Optional.ofNullable(bankService.findCustomerById(customerId))
                .map(customerDTOMapper::map)
                .orElse(null));
    }

    @Override
    public ResponseEntity<List<AccountDTO>> findAllAccounts() {
        return ResponseEntity.ok(accountDTOMapper.map(bankService.findAllAccounts()));
    }

    @Override
    public ResponseEntity<AccountDTO> findAccountById(Long accountNumber) {
        return ResponseEntity.ok(Optional.ofNullable(bankService.findAccountByAccountNumber(accountNumber))
                .map(accountDTOMapper::map)
                .orElse(null));
    }

    @Override
    public ResponseEntity<List<TransactionDTO>> findAllTransaction() {
        return ResponseEntity.ok(transactionDTOMapper.map(bankService.findAllTransaction()));
    }

    @Override
    public ResponseEntity<TransactionDTO> findTransactionByTransactionId(Long transactionId) {
        return ResponseEntity.ok(Optional.ofNullable(bankService.findTransactionByTransactionId(transactionId))
                .map(transactionDTOMapper::map)
                .orElse(null));
    }
}
