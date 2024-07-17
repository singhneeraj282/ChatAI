package com.tera.ChatAI.controller;

import com.tera.ChatAI.dto.*;
import com.tera.ChatAI.entity.CustomerMasterData;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@RequestMapping(value = "/api/v1/segment")
public interface SegmentAPI {    @GetMapping(value = "/customer-master-data")
ResponseEntity<List<CustomerMasterData>> findCustomerMasterData();

    @GetMapping(value = "/customer-master-data/{id}")
    ResponseEntity<CustomerMasterData> findCustomerMasterDataById(@PathVariable Long id);

    @GetMapping(value = "/populate-customer-master-data")
    ResponseEntity<CustomerMasterData> populateCustomerMasterData();

}
