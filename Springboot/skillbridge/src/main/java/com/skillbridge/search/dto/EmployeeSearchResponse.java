package com.skillbridge.search.dto;

import java.util.List;

public class EmployeeSearchResponse {

    private Long employeeId;
    private String name;
    private String department;
    private String designation;

    private Integer totalAllocation;
    private List<String> approvedSkills;
    private List<ProjectInfo> projects;

    // -------- inner class --------
    public static class ProjectInfo {
        public Long projectId;
        public String projectName;
        public Integer allocationPercent;
        public String billingType;
    }

    // -------- getters & setters --------

    public Long getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Long employeeId) {
        this.employeeId = employeeId;
    }

    public String getName() {
        return name;
    }

    public Integer getTotalAllocation() {
        return totalAllocation;
    }

    public List<String> getApprovedSkills() {
        return approvedSkills;
    }

    public List<ProjectInfo> getProjects() {
        return projects;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setTotalAllocation(Integer totalAllocation) {
        this.totalAllocation = totalAllocation;
    }

    public void setApprovedSkills(List<String> approvedSkills) {
        this.approvedSkills = approvedSkills;
    }

    public void setProjects(List<ProjectInfo> projects) {
        this.projects = projects;
    }

    public String getDepartment() {
        return department;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }
}
