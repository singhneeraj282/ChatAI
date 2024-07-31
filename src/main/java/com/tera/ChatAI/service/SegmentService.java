package com.tera.ChatAI.service;

import com.tera.ChatAI.bo.CustomerBO;
import com.tera.ChatAI.entity.CustomerMasterData;
import com.tera.ChatAI.repository.CustomerMasterDataRepository;
import com.tera.ChatAI.repository.SegmentDefinitionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.Optional;
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
        var alreadyCreatedCustomerIds = customerMasterDataRepository.findAll().stream()
                .map(CustomerMasterData::getCustomerId)
                .toList();
        var customersMasterDatas = customers.stream()
                .filter(customer -> !alreadyCreatedCustomerIds.contains(customer.getId()))
                .map(this::mapToCustomerMasterData)
                .collect(Collectors.toList());
        return customerMasterDataRepository.saveAll(customersMasterDatas);
    }

    public CustomerMasterData populateCustomerMasterDataById(Long id) {
        var customer = Optional.ofNullable(bankService.findCustomerById(id))
                .orElseThrow(() -> new RuntimeException("User not found"));
        var customerMasterData = customerMasterDataRepository.findById(id)
                .orElseGet(() -> mapToCustomerMasterData(customer));
        return customerMasterDataRepository.save(customerMasterData);
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
