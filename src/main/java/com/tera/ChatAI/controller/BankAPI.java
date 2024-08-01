package com.tera.ChatAI.controller;

import com.tera.ChatAI.dto.*;
import com.tera.ChatAI.entity.CustomerMasterData;
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
    ResponseEntity<CustomerDTO> findCustomerById(@PathVariable Long customerId);

    @GetMapping(value = "/accounts")
    ResponseEntity<List<AccountDTO>> findAllAccounts();

    @GetMapping(value = "/accounts/{accountNumber}")
    ResponseEntity<AccountDTO> findAccountById(@PathVariable Long accountNumber);

    @GetMapping(value = "/transactions")
    ResponseEntity<List<TransactionDTO>> findAllTransaction();

    @GetMapping(value = "/transactions/{transactionId}")
    ResponseEntity<TransactionDTO> findTransactionByTransactionId(@PathVariable Long transactionId);

    @GetMapping(value = "/credit-cards")
    ResponseEntity<List<CreditCardDTO>> findAllCreditCards();

    @GetMapping(value = "/credit-cards/{id}")
    ResponseEntity<CreditCardDTO> findCreditCardById(@PathVariable Long id);

    @GetMapping(value = "/debit-cards")
    ResponseEntity<List<DebitCardDTO>> findAllDebitCards();

    @GetMapping(value = "/debit-cards/{id}")
    ResponseEntity<DebitCardDTO> findDebitCardById(@PathVariable Long id);


}
