package com.tera.ChatAI.service;

import com.tera.ChatAI.entity.CustomerMasterData;
import com.tera.ChatAI.repository.CustomerMasterDataRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SegmentService {
    private final CustomerMasterDataRepository customerMasterDataRepository;


    public List<CustomerMasterData> findCustomerMasterData() {
        return customerMasterDataRepository.findAll();
    }

    public CustomerMasterData findCustomerMasterDataById(Long id) {
        return customerMasterDataRepository.findById(id)
                .orElse(null);
    }

    public CustomerMasterData populateCustomerMasterData() {
        return null;
    }
}
