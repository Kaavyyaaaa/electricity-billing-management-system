package com.sprint2.electricity_billing_system.controller;

import com.sprint2.electricity_billing_system.service.InvoiceService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/invoices")
public class InvoiceController {

    private final InvoiceService invoiceService;

    public InvoiceController(
            InvoiceService invoiceService) {

        this.invoiceService = invoiceService;
    }

    // ==========================================
    // US006 - GENERATE INVOICE
    // ==========================================

    @GetMapping("/transaction/{transactionId}")
    public ResponseEntity<?> generateInvoice(
            @PathVariable String transactionId) {

        try {

            Map<String, Object> invoice =
                    invoiceService.generateInvoice(
                            transactionId);

            return ResponseEntity.ok(invoice);

        } catch (IllegalArgumentException e) {

            return ResponseEntity
                    .badRequest()
                    .body(e.getMessage());

        } catch (Exception e) {

            return ResponseEntity
                    .internalServerError()
                    .body("Unable to generate invoice");
        }
    }
}