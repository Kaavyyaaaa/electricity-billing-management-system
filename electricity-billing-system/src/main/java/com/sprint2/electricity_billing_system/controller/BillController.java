package com.sprint2.electricity_billing_system.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sprint2.electricity_billing_system.dto.BillResponse;
import com.sprint2.electricity_billing_system.dto.BillSummaryRequest;
import com.sprint2.electricity_billing_system.dto.BillSummaryResponse;
import com.sprint2.electricity_billing_system.service.BillService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:4200")
public class BillController {

    private final BillService billService;

    public BillController(BillService billService) {
        this.billService = billService;
    }

    @GetMapping("/bills/{consumerNumber}")
    public ResponseEntity<List<BillResponse>> getBills(
            @PathVariable String consumerNumber) {

        List<BillResponse> response =
                billService.getBillsByConsumerNumber(consumerNumber);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/bill-summary")
    public ResponseEntity<BillSummaryResponse> getBillSummary(
            @Valid @RequestBody BillSummaryRequest request) {

        BillSummaryResponse response =
                billService.getBillSummary(request);

        return ResponseEntity.ok(response);
    }
}