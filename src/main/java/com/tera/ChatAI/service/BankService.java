package com.tera.ChatAI.service;

import com.tera.ChatAI.bo.AccountBO;
import com.tera.ChatAI.bo.CustomerBO;
import com.tera.ChatAI.bo.TransactionBO;
import com.tera.ChatAI.mapper.bo.AccountBOMapper;
import com.tera.ChatAI.mapper.bo.CustomerBOMapper;
import com.tera.ChatAI.mapper.bo.TransactionBOMapper;
import com.tera.ChatAI.mapper.dto.CustomerDTOMapper;
import com.tera.ChatAI.repository.AccountRepository;
import com.tera.ChatAI.repository.CustomerRepository;
import com.tera.ChatAI.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BankService {

    private final AccountRepository accountRepository;
    private final CustomerRepository customerRepository;
    private final TransactionRepository transactionRepository;
    private final AccountBOMapper accountBOMapper;
    private final CustomerBOMapper customerBOMapper;
    private final TransactionBOMapper transactionBOMapper;

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
        return accountRepository.findByAccountNumber(accountNumber)
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

}
