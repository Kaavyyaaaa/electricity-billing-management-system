package com.sprint2.electricity_billing_system.controller;

import com.sprint2.electricity_billing_system.entity.Bill;
import com.sprint2.electricity_billing_system.service.AdminBillHistoryService;
import com.sprint2.electricity_billing_system.service.BillExportService;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/admin/bills")
public class AdminBillHistoryController {

    private final AdminBillHistoryService adminBillHistoryService;
    private final BillExportService billExportService;

    public AdminBillHistoryController(
            AdminBillHistoryService adminBillHistoryService,
            BillExportService billExportService) {

        this.adminBillHistoryService = adminBillHistoryService;
        this.billExportService = billExportService;
    }

    // =====================================================
    // US016 - VIEW BILL HISTORY BY CUSTOMER ID
    // =====================================================

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<?> getCustomerBills(
            @PathVariable Long customerId) {

        try {

            List<Bill> bills =
                    adminBillHistoryService
                            .getByCustomerId(customerId);

            return ResponseEntity.ok(bills);

        } catch (IllegalArgumentException e) {

            return ResponseEntity
                    .badRequest()
                    .body(e.getMessage());

        } catch (Exception e) {

            return ResponseEntity
                    .internalServerError()
                    .body("Unable to retrieve bill history");
        }
    }

    // =====================================================
    // US016 - VIEW BILL HISTORY BY CONSUMER NUMBER
    // =====================================================

    @GetMapping("/consumer/{consumerNumber}")
    public ResponseEntity<?> getConsumerBills(
            @PathVariable String consumerNumber) {

        try {

            List<Bill> bills =
                    adminBillHistoryService
                            .getByConsumerNumber(
                                    consumerNumber);

            return ResponseEntity.ok(bills);

        } catch (IllegalArgumentException e) {

            return ResponseEntity
                    .badRequest()
                    .body(e.getMessage());

        } catch (Exception e) {

            return ResponseEntity
                    .internalServerError()
                    .body("Unable to retrieve bill history");
        }
    }

    // =====================================================
    // US016 - SEARCH / FILTER BILL HISTORY
    // =====================================================

    @GetMapping("/search/{customerId}")
    public ResponseEntity<?> searchBills(

            @PathVariable Long customerId,

            @RequestParam(required = false)
            String startDate,

            @RequestParam(required = false)
            String endDate,

            @RequestParam(required = false)
            String status) {

        try {

            LocalDate start = null;
            LocalDate end = null;

            // Convert start date
            if (startDate != null &&
                    !startDate.trim().isEmpty()) {

                start = LocalDate.parse(startDate);
            }

            // Convert end date
            if (endDate != null &&
                    !endDate.trim().isEmpty()) {

                end = LocalDate.parse(endDate);
            }

            List<Bill> bills =
                    adminBillHistoryService.search(
                            customerId,
                            start,
                            end,
                            status);

            return ResponseEntity.ok(bills);

        } catch (java.time.format.DateTimeParseException e) {

            return ResponseEntity
                    .badRequest()
                    .body("Date must be in YYYY-MM-DD format");

        } catch (IllegalArgumentException e) {

            return ResponseEntity
                    .badRequest()
                    .body(e.getMessage());

        } catch (Exception e) {

            return ResponseEntity
                    .internalServerError()
                    .body("Unable to search bill history");
        }
    }

    // =====================================================
    // US016 - EXPORT BILL HISTORY AS CSV
    // =====================================================

    @GetMapping("/export/{customerId}")
    public ResponseEntity<?> exportBills(

            @PathVariable Long customerId,

            @RequestParam(required = false)
            String startDate,

            @RequestParam(required = false)
            String endDate,

            @RequestParam(required = false)
            String status) {

        try {

            LocalDate start = null;
            LocalDate end = null;

            // Convert start date
            if (startDate != null &&
                    !startDate.trim().isEmpty()) {

                start = LocalDate.parse(startDate);
            }

            // Convert end date
            if (endDate != null &&
                    !endDate.trim().isEmpty()) {

                end = LocalDate.parse(endDate);
            }

            // Get filtered bills
            List<Bill> bills =
                    adminBillHistoryService.search(
                            customerId,
                            start,
                            end,
                            status);

            // Convert bills to CSV
            byte[] csv =
                    billExportService.exportToCsv(bills);

            return ResponseEntity.ok()
                    .header(
                            HttpHeaders.CONTENT_DISPOSITION,
                            "attachment; filename=bill-history.csv")
                    .contentType(
                            MediaType.parseMediaType(
                                    "text/csv"))
                    .body(csv);

        } catch (java.time.format.DateTimeParseException e) {

            return ResponseEntity
                    .badRequest()
                    .body("Date must be in YYYY-MM-DD format");

        } catch (IllegalArgumentException e) {

            return ResponseEntity
                    .badRequest()
                    .body(e.getMessage());

        } catch (Exception e) {

            return ResponseEntity
                    .internalServerError()
                    .body("Unable to export bill history");
        }
    }
}