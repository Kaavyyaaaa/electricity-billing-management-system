package com.sprint2.electricity_billing_system.service;

import com.sprint2.electricity_billing_system.entity.Bill;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BillExportService {

    public byte[] exportToCsv(List<Bill> bills) {

        StringBuilder csv = new StringBuilder();

        // Header
        csv.append("Bill ID,")
           .append("Customer ID,")
           .append("Bill Number,")
           .append("Billing Period,")
           .append("Bill Date,")
           .append("Due Date,")
           .append("Disconnection Date,")
           .append("Bill Amount,")
           .append("Late Fee,")
           .append("Units Consumed,")
           .append("Status\n");

        // Data
        for (Bill bill : bills) {

            csv.append(value(bill.getBillId())).append(",");
            csv.append(value(bill.getCustomerId())).append(",");
            csv.append(value(bill.getBillNumber())).append(",");
            csv.append(value(bill.getBillingPeriod())).append(",");
            csv.append(value(bill.getBillDate())).append(",");
            csv.append(value(bill.getDueDate())).append(",");
            csv.append(value(bill.getDisconnectionDate())).append(",");
            csv.append(value(bill.getBillAmount())).append(",");
            csv.append(value(bill.getLateFee())).append(",");
            csv.append(value(bill.getUnitsConsumed())).append(",");
            csv.append(value(bill.getStatus())).append("\n");
        }

        return csv.toString().getBytes(
                java.nio.charset.StandardCharsets.UTF_8);
    }

    private String value(Object value) {

        if (value == null) {
            return "";
        }

        String text = String.valueOf(value);

        // Escape commas/quotes for CSV
        if (text.contains(",") ||
                text.contains("\"") ||
                text.contains("\n")) {

            text = text.replace("\"", "\"\"");
            return "\"" + text + "\"";
        }

        return text;
    }
}