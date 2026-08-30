package com.sprint2.electricity_billing_system.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "SME")
public class Sme {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "SME_ID")
    private Long smeId;

    @Column(name = "USER_ID", unique = true)
    private Long userId;

    @Column(name = "SME_NAME")
    private String smeName;

    @Column(name = "DEPARTMENT")
    private String department;

    public Sme() {
    }

    public Long getSmeId() {
        return smeId;
    }

    public void setSmeId(Long smeId) {
        this.smeId = smeId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getSmeName() {
        return smeName;
    }

    public void setSmeName(String smeName) {
        this.smeName = smeName;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }
}