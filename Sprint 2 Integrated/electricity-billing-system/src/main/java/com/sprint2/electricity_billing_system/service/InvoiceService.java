package com.sprint2.electricity_billing_system.service;

import com.sprint2.electricity_billing_system.entity.Bill;
import com.sprint2.electricity_billing_system.entity.Customer;
import com.sprint2.electricity_billing_system.entity.Invoice;
import com.sprint2.electricity_billing_system.entity.Payment;
import com.sprint2.electricity_billing_system.entity.PaymentBill;
import com.sprint2.electricity_billing_system.repository.BillRepository;
import com.sprint2.electricity_billing_system.repository.CustomerRepository;
import com.sprint2.electricity_billing_system.repository.InvoiceRepository;
import com.sprint2.electricity_billing_system.repository.PaymentBillRepository;
import com.sprint2.electricity_billing_system.repository.PaymentRepository;

import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.Map;

@Service
public class InvoiceService {

    private final InvoiceRepository invoiceRepository;
    private final PaymentRepository paymentRepository;
    private final PaymentBillRepository paymentBillRepository;
    private final BillRepository billRepository;
    private final CustomerRepository customerRepository;

    public InvoiceService(
            InvoiceRepository invoiceRepository,
            PaymentRepository paymentRepository,
            PaymentBillRepository paymentBillRepository,
            BillRepository billRepository,
            CustomerRepository customerRepository) {

        this.invoiceRepository = invoiceRepository;
        this.paymentRepository = paymentRepository;
        this.paymentBillRepository = paymentBillRepository;
        this.billRepository = billRepository;
        this.customerRepository = customerRepository;
    }

    public Map<String, Object> generateInvoice(String transactionId) {

        if (transactionId == null ||
                transactionId.trim().isEmpty()) {

            throw new IllegalArgumentException(
                    "Transaction ID is required");
        }

        Payment payment = paymentRepository
                .findByTransactionId(transactionId)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Transaction ID not found"));

        if (!"SUCCESS".equalsIgnoreCase(
                payment.getPaymentStatus())) {

            throw new IllegalArgumentException(
                    "Invoice cannot be generated for failed payment");
        }

        Invoice invoice = invoiceRepository
                .findByPaymentId(payment.getPaymentId())
                .orElse(null);

        if (invoice == null) {

            invoice = new Invoice();

            invoice.setPaymentId(
                    payment.getPaymentId());

            invoice.setInvoiceNumber(
                    "INV-" + System.currentTimeMillis());

            invoice.setInvoiceDate(LocalDate.now());

            invoice = invoiceRepository.save(invoice);
        }

        PaymentBill paymentBill =
                paymentBillRepository
                        .findByPaymentId(
                                payment.getPaymentId())
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Bill details not found for payment"));

        Bill bill = billRepository
                .findById(paymentBill.getBillId())
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Bill not found"));

        Customer customer = customerRepository
                .findById(bill.getCustomerId())
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Customer not found"));

        Map<String, Object> details =
                new LinkedHashMap<>();

        details.put("invoiceNumber",
                invoice.getInvoiceNumber());

        details.put("invoiceDate",
                invoice.getInvoiceDate());

        details.put("paymentId",
                payment.getPaymentId());

        details.put("transactionId",
                payment.getTransactionId());

        details.put("receiptNumber",
                payment.getReceiptNumber());

        details.put("consumerNumber",
                customer.getConsumerNumber());

        details.put("customerId",
                customer.getCustomerId());

        details.put("customerName",
                customer.getFullName());

        details.put("customerAddress",
                customer.getAddress());

        details.put("customerEmail",
                customer.getEmail());

        details.put("customerMobile",
                customer.getMobileNumber());

        details.put("transactionDate",
                payment.getPaymentDate());

        details.put("transactionType",
                payment.getTransactionType());

        details.put("paymentMethod",
                payment.getPaymentMethod());

        details.put("billNumber",
                bill.getBillNumber());

        details.put("billingPeriod",
                bill.getBillingPeriod());

        details.put("billDate",
                bill.getBillDate());

        details.put("dueDate",
                bill.getDueDate());

        details.put("transactionAmount",
                payment.getTotalAmount());

        details.put("paymentStatus",
                payment.getPaymentStatus());

        return details;
    }
}