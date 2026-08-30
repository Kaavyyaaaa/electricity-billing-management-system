package com.sprint2.electricity_billing_system.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "COMPLAINT")
public class Complaint {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "COMPLAINT_ID")
    private Long complaintId;

    @Column(name = "CUSTOMER_ID")
    private Long customerId;

    @Column(name = "COMPLAINT_TYPE")
    private String complaintType;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "CONTACT_METHOD")
    private String contactMethod;

    @Column(name = "DATE_SUBMITTED")
    private LocalDate dateSubmitted;

    @Column(name = "STATUS")
    private String status;

    @Column(name = "REMARKS")
    private String remarks;

    @Column(name = "ASSIGNED_SME_ID")
    private Long assignedSmeId;

    @Column(name = "LAST_UPDATED")
    private LocalDate lastUpdated;

    public Complaint() {
    }

    public Long getComplaintId() {
        return complaintId;
    }

    public void setComplaintId(Long complaintId) {
        this.complaintId = complaintId;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public String getComplaintType() {
        return complaintType;
    }

    public void setComplaintType(String complaintType) {
        this.complaintType = complaintType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getContactMethod() {
        return contactMethod;
    }

    public void setContactMethod(String contactMethod) {
        this.contactMethod = contactMethod;
    }

    public LocalDate getDateSubmitted() {
        return dateSubmitted;
    }

    public void setDateSubmitted(LocalDate dateSubmitted) {
        this.dateSubmitted = dateSubmitted;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public Long getAssignedSmeId() {
        return assignedSmeId;
    }

    public void setAssignedSmeId(Long assignedSmeId) {
        this.assignedSmeId = assignedSmeId;
    }

    public LocalDate getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(LocalDate lastUpdated) {
        this.lastUpdated = lastUpdated;
    }
}