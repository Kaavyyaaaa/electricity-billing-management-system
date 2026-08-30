package com.sprint2.electricity_billing_system.controller;

import com.sprint2.electricity_billing_system.dto.PaymentRequest;
import com.sprint2.electricity_billing_system.entity.Bill;
import com.sprint2.electricity_billing_system.entity.Payment;
import com.sprint2.electricity_billing_system.service.BillHistoryService;
import com.sprint2.electricity_billing_system.service.BillService;
import com.sprint2.electricity_billing_system.service.PaymentService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/bills")
public class BillController {

    private final BillService billService;
    private final BillHistoryService billHistoryService;
    private final PaymentService paymentService;

    public BillController(
            BillService billService,
            BillHistoryService billHistoryService,
            PaymentService paymentService) {

        this.billService = billService;
        this.billHistoryService = billHistoryService;
        this.paymentService = paymentService;
    }

    // =====================================================
    // US015 - ADMIN ADD BILL
    // =====================================================

    @PostMapping
    public ResponseEntity<?> addBill(
            @RequestBody Bill bill) {

        try {

            Bill savedBill =
                    billService.addBill(bill);

            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(savedBill);

        } catch (IllegalArgumentException e) {

            return ResponseEntity
                    .badRequest()
                    .body(e.getMessage());
        }
    }

    // =====================================================
    // US007 - CUSTOMER BILL HISTORY
    // =====================================================

    @GetMapping("/history/{customerId}")
    public ResponseEntity<?> getBillHistory(

            @PathVariable Long customerId,

            @RequestParam(required = false)
            String startDate,

            @RequestParam(required = false)
            String endDate,

            @RequestParam(required = false)
            String status,

            @RequestParam(required = false)
            String sortBy) {

        try {

            LocalDate start = null;
            LocalDate end = null;

            if (startDate != null &&
                    !startDate.trim().isEmpty()) {

                start = LocalDate.parse(startDate);
            }

            if (endDate != null &&
                    !endDate.trim().isEmpty()) {

                end = LocalDate.parse(endDate);
            }

            List<Bill> history =
                    billHistoryService.getBillHistory(
                            customerId,
                            start,
                            end,
                            status,
                            sortBy);

            return ResponseEntity.ok(history);

        } catch (IllegalArgumentException e) {

            return ResponseEntity
                    .badRequest()
                    .body(e.getMessage());

        } catch (Exception e) {

            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Unable to retrieve bill history");
        }
    }

    // =====================================================
    // US005 - PAY BILL
    // =====================================================

    @PostMapping("/pay")
    public ResponseEntity<?> payBill(
            @RequestBody PaymentRequest request) {

        try {

            Payment payment =
                    paymentService.makePayment(request);

            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(payment);

        } catch (IllegalArgumentException e) {

            return ResponseEntity
                    .badRequest()
                    .body(e.getMessage());

        } catch (Exception e) {

            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Payment failed");
        }
    }
}