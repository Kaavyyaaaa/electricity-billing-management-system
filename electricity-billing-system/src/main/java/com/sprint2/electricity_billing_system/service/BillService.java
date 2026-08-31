package com.sprint2.electricity_billing_system.service;

import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sprint2.electricity_billing_system.dto.BillResponse;
import com.sprint2.electricity_billing_system.dto.BillSummaryRequest;
import com.sprint2.electricity_billing_system.dto.BillSummaryResponse;
import com.sprint2.electricity_billing_system.entity.Bill;
import com.sprint2.electricity_billing_system.entity.Customer;
import com.sprint2.electricity_billing_system.exception.InvalidRequestException;
import com.sprint2.electricity_billing_system.exception.ResourceNotFoundException;
import com.sprint2.electricity_billing_system.repository.BillRepository;

@Service
public class BillService {

    private static final DateTimeFormatter BILLING_PERIOD_FORMAT =
            DateTimeFormatter.ofPattern("MMMM yyyy", Locale.ENGLISH);

    private final BillRepository billRepository;
    private final CustomerService customerService;

    public BillService(
            BillRepository billRepository,
            CustomerService customerService) {

        this.billRepository = billRepository;
        this.customerService = customerService;
    }

    @Transactional(readOnly = true)
    public List<BillResponse> getBillsByConsumerNumber(
            String consumerNumber) {

        Customer customer =
                customerService.findCustomerByConsumerNumber(
                        consumerNumber
                );

        List<Bill> bills =
                billRepository.findByCustomerIdOrderByBillDateDesc(
                        customer.getCustomerId()
                );

        return bills.stream()
                .map(this::convertToBillResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public BillSummaryResponse getBillSummary(
            BillSummaryRequest request) {

        Customer customer =
                customerService.findCustomerByConsumerNumber(
                        request.getConsumerNumber()
                );

        validateDuplicateBillIds(request.getBillIds());

        List<Bill> selectedBills =
                billRepository.findByBillIdIn(request.getBillIds());

        validateAllBillsExist(
                request.getBillIds(),
                selectedBills
        );

        validateBillOwnership(
                customer.getCustomerId(),
                selectedBills
        );

        List<BillResponse> selectedBillResponses =
                selectedBills.stream()
                        .map(this::convertToBillResponse)
                        .toList();

        double totalAmount = selectedBills.stream()
                .map(Bill::getBillAmount)
                .filter(amount -> amount != null)
                .mapToDouble(Double::doubleValue)
                .sum();

        return new BillSummaryResponse(
                customer.getConsumerNumber(),
                selectedBills.size(),
                selectedBillResponses,
                totalAmount
        );
    }

    private void validateDuplicateBillIds(
            List<Long> billIds) {

        Set<Long> uniqueBillIds = new HashSet<>(billIds);

        if (uniqueBillIds.size() != billIds.size()) {
            throw new InvalidRequestException(
                    "Duplicate bill IDs are not allowed"
            );
        }
    }

    private void validateAllBillsExist(
            List<Long> requestedBillIds,
            List<Bill> selectedBills) {

        if (selectedBills.size() != requestedBillIds.size()) {
            throw new ResourceNotFoundException(
                    "One or more selected bills were not found"
            );
        }
    }

    private void validateBillOwnership(
            Long customerId,
            List<Bill> selectedBills) {

        boolean invalidOwnership = selectedBills.stream()
                .anyMatch(bill ->
                        bill.getCustomerId() == null
                        || !bill.getCustomerId().equals(customerId)
                );

        if (invalidOwnership) {
            throw new InvalidRequestException(
                    "One or more selected bills do not belong "
                            + "to the specified customer"
            );
        }
    }

    private BillResponse convertToBillResponse(
            Bill bill) {

        String billingPeriod = null;

        if (bill.getBillDate() != null) {
            billingPeriod = bill.getBillDate()
                    .format(BILLING_PERIOD_FORMAT);
        }

        return new BillResponse(
                bill.getBillId(),
                bill.getBillNumber(),
                bill.getBillDate(),
                billingPeriod,
                bill.getDueDate(),
                bill.getBillAmount(),
                bill.getUnitsConsumed(),
                bill.getStatus()
        );
    }
}