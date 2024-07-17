package com.tera.ChatAI.service;

import com.tera.ChatAI.bo.*;
import com.tera.ChatAI.mapper.bo.*;
import com.tera.ChatAI.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BankService {

    @Lazy
    private final AccountRepository accountRepository;
    @Lazy
    private final CustomerRepository customerRepository;
    @Lazy
    private final TransactionRepository transactionRepository;
    @Lazy
    private final CreditCardRepository creditCardRepository;
    @Lazy
    private final DebitCardRepository debitCardRepository;

    @Lazy
    private final AccountBOMapper accountBOMapper;
    @Lazy
    private final CustomerBOMapper customerBOMapper;
    @Lazy
    private final TransactionBOMapper transactionBOMapper;
    @Lazy
    private final CreditCardBOMapper creditCardBOMapper;
    @Lazy
    private final DebitCardBOMapper debitCardBOMapper;


    public List<CustomerBO> findAllCustomers() {
        return customerBOMapper.map(customerRepository.findAll());
    }

    public CustomerBO findCustomerById(Long customerId) {
        return customerRepository.findById(customerId)
                .map(customerBOMapper::map)
                .orElse(null);

    }

    public List<AccountBO> findAllAccounts() {
        return accountBOMapper.map(accountRepository.findAll());
    }

    public AccountBO findAccountByAccountNumber(Long accountNumber) {
        return accountRepository.findByAccountId(accountNumber)
                .map(accountBOMapper::map)
                .orElse(null);
    }

    public List<TransactionBO> findAllTransaction() {
        return transactionBOMapper.map(transactionRepository.findAll());
    }

    public TransactionBO findTransactionByTransactionId(Long transactionId) {
        return transactionRepository.findById(transactionId)
                .map(transactionBOMapper::map)
                .orElse(null);
    }

    public DebitCardBO findDebitCardById(Long id) {
        return debitCardRepository.findById(id)
                .map(debitCardBOMapper::map)
                .orElse(null);
    }

    public List<DebitCardBO> findAllDebitCards() {
        return debitCardBOMapper.map(debitCardRepository.findAll());
    }

    public CreditCardBO findCreditCardById(Long id) {
        return creditCardRepository.findById(id)
                .map(creditCardBOMapper::map)
                .orElse(null);
    }

    public List<CreditCardBO> findAllCreditCards() {
        return creditCardBOMapper.map(creditCardRepository.findAll());
    }

}
