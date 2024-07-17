package com.tera.ChatAI.controller.impl;

import com.tera.ChatAI.controller.SegmentAPI;
import com.tera.ChatAI.entity.CustomerMasterData;
import com.tera.ChatAI.service.SegmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class SegmentController implements SegmentAPI {

    private final SegmentService segmentService;

    @Override
    public ResponseEntity<List<CustomerMasterData>> findCustomerMasterData() {
        return ResponseEntity.ok(segmentService.findCustomerMasterData());
    }

    @Override
    public ResponseEntity<CustomerMasterData> findCustomerMasterDataById(Long id) {
        return ResponseEntity.ok(segmentService.findCustomerMasterDataById(id));
    }

    @Override
    public ResponseEntity<CustomerMasterData> populateCustomerMasterData() {
        return ResponseEntity.ok(segmentService.populateCustomerMasterData());
    }
}
