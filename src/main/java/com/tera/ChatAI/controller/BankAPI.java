package com.tera.ChatAI.controller;

import com.tera.ChatAI.dto.AccountDTO;
import com.tera.ChatAI.dto.CustomerDTO;
import com.tera.ChatAI.dto.TransactionDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@RequestMapping(value = "/api/v1/bank")
public interface BankAPI {
    @GetMapping(value = "/customers")
    ResponseEntity<List<CustomerDTO>> findAllCustomers();

    @GetMapping(value = "/customers/{customerId}")
    ResponseEntity<CustomerDTO> findCustomerById(@PathVariable String customerId);

    @GetMapping(value = "/accounts")
    ResponseEntity<List<AccountDTO>> findAllAccounts();

    @GetMapping(value = "/accounts/{accountId}")
    ResponseEntity<AccountDTO> findAccountById(@PathVariable String accountId);

    @GetMapping(value = "/transactions")
    ResponseEntity<List<TransactionDTO>> findAllTransaction();

    @GetMapping(value = "/transactions/{transactionId}")
    ResponseEntity<TransactionDTO> findAllTransaction(@PathVariable String transactionId);

}
