package com.skillbridge.project.dto;

import java.util.List;

public class GapAnalysisResponse {

    private Long projectId;
    private String projectName;

    private List<String> requiredSkills;
    private List<String> employeeSkills;
    private List<String> missingSkills;

    // -------- getters & setters --------

    public Long getProjectId() {
        return projectId;
    }

    public String getProjectName() {
        return projectName;
    }

    public List<String> getRequiredSkills() {
        return requiredSkills;
    }

    public List<String> getEmployeeSkills() {
        return employeeSkills;
    }

    public List<String> getMissingSkills() {
        return missingSkills;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public void setRequiredSkills(List<String> requiredSkills) {
        this.requiredSkills = requiredSkills;
    }

    public void setEmployeeSkills(List<String> employeeSkills) {
        this.employeeSkills = employeeSkills;
    }

    public void setMissingSkills(List<String> missingSkills) {
        this.missingSkills = missingSkills;
    }
}
