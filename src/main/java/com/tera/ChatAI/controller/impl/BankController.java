package com.tera.ChatAI.controller.impl;

import com.tera.ChatAI.controller.BankAPI;
import com.tera.ChatAI.dto.AccountDTO;
import com.tera.ChatAI.dto.CustomerDTO;
import com.tera.ChatAI.dto.TransactionDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class BankController implements BankAPI {


    @Override
    public ResponseEntity<List<CustomerDTO>> findAllCustomers() {
        return null;
    }

    @Override
    public ResponseEntity<CustomerDTO> findCustomerById(String customerId) {
        return null;
    }

    @Override
    public ResponseEntity<List<AccountDTO>> findAllAccounts() {
        return null;
    }

    @Override
    public ResponseEntity<AccountDTO> findAccountById(String accountId) {
        return null;
    }

    @Override
    public ResponseEntity<List<TransactionDTO>> findAllTransaction() {
        return null;
    }

    @Override
    public ResponseEntity<TransactionDTO> findAllTransaction(String transactionId) {
        return null;
    }
}
