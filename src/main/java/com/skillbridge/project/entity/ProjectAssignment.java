package com.skillbridge.project.entity;

import com.skillbridge.common.enums.BillingType;
import com.skillbridge.employee.entity.Employee;
import jakarta.persistence.*;

@Entity
@Table(name = "project_assignments")
public class ProjectAssignment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;

    @ManyToOne
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;

    // Percentage allocation (e.g. 40, 60)
    private Integer allocationPercent;

    @Enumerated(EnumType.STRING)
    private BillingType billingType;

    // -------- Getters & Setters --------

    public Long getId() {
        return id;
    }

    public Employee getEmployee() {
        return employee;
    }

    public Project getProject() {
        return project;
    }

    public Integer getAllocationPercent() {
        return allocationPercent;
    }

    public BillingType getBillingType() {
        return billingType;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public void setAllocationPercent(Integer allocationPercent) {
        this.allocationPercent = allocationPercent;
    }

    public void setBillingType(BillingType billingType) {
        this.billingType = billingType;
    }
}
