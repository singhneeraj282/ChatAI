package com.tera.ChatAI.service;

import com.tera.ChatAI.bo.CustomerBO;
import com.tera.ChatAI.entity.CustomerMasterData;
import com.tera.ChatAI.repository.CustomerMasterDataRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SegmentService {

    private final CustomerMasterDataRepository customerMasterDataRepository;
    private final BankService bankService;

    public List<CustomerMasterData> findCustomerMasterData() {
        return customerMasterDataRepository.findAll();
    }

    public CustomerMasterData findCustomerMasterDataById(Long id) {
        return customerMasterDataRepository.findById(id)
                .orElse(null);
    }

    public List<CustomerMasterData> populateCustomerMasterData() {
        var customers = bankService.findAllCustomers();
        var customersMasterData = customers.stream()
                .map(this::mapToCustomerMasterData)
                .collect(Collectors.toList());
        customerMasterDataRepository.saveAll(customersMasterData);

        return customersMasterData;
    }

    private CustomerMasterData mapToCustomerMasterData(CustomerBO customerBO) {
        return CustomerMasterData.builder()
                .customerId(customerBO.getId())
                .age(Period.between(customerBO.getDateOfBirth(), LocalDate.now()).getYears())
                .avgMonthlyBalance(customerBO.avgBalance())
                .totalInflows(customerBO.totalInflow())
                .totalOutflows(customerBO.totalOutflow())
                .customerMerchantCategories(customerBO.merchantCategories())
                .build();
    }
}
