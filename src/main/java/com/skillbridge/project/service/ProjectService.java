package com.skillbridge.project.service;

import com.skillbridge.common.enums.BillingType;
import com.skillbridge.common.enums.ProjectStatus;
import com.skillbridge.employee.entity.Employee;
import com.skillbridge.project.entity.Project;
import com.skillbridge.project.entity.ProjectAssignment;
import com.skillbridge.project.entity.ProjectSkill;
import com.skillbridge.project.repository.ProjectAssignmentRepository;
import com.skillbridge.project.repository.ProjectRepository;
import com.skillbridge.project.repository.ProjectSkillRepository;
import com.skillbridge.skill.entity.Skill;
import com.skillbridge.skill.repository.EmployeeSkillRepository;
import com.skillbridge.skill.repository.SkillRepository;
import com.skillbridge.user.entity.User;
import org.springframework.stereotype.Service;

import com.skillbridge.common.enums.SkillStatus;
import com.skillbridge.project.dto.GapAnalysisResponse;
import com.skillbridge.skill.entity.EmployeeSkill;


import java.util.List;

@Service
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final ProjectSkillRepository projectSkillRepository;
    private final ProjectAssignmentRepository projectAssignmentRepository;
    private final SkillRepository skillRepository;
    private final EmployeeSkillRepository employeeSkillRepository;


    public ProjectService(ProjectRepository projectRepository,
                          ProjectSkillRepository projectSkillRepository,
                          ProjectAssignmentRepository projectAssignmentRepository,
                          SkillRepository skillRepository,
                          EmployeeSkillRepository employeeSkillRepository) {

        this.projectRepository = projectRepository;
        this.projectSkillRepository = projectSkillRepository;
        this.projectAssignmentRepository = projectAssignmentRepository;
        this.skillRepository = skillRepository;
        this.employeeSkillRepository = employeeSkillRepository;
    }


    // 1️⃣ Create project (HR / Superuser)
    public Project createProject(String name,
                                 String description,
                                 ProjectStatus status,
                                 User createdBy) {

        Project project = new Project();
        project.setName(name);
        project.setDescription(description);
        project.setStatus(status);
        project.setCreatedBy(createdBy);

        return projectRepository.save(project);
    }

    // 2️⃣ Add required skill to project
    public ProjectSkill addRequiredSkill(Long projectId, String skillName) {

        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found"));

        Skill skill = skillRepository.findByName(skillName)
                .orElseThrow(() -> new RuntimeException("Skill not found"));

        ProjectSkill projectSkill = new ProjectSkill();
        projectSkill.setProject(project);
        projectSkill.setSkill(skill);

        return projectSkillRepository.save(projectSkill);
    }

    // 3️⃣ Assign employee to project with allocation
    public ProjectAssignment assignEmployee(Employee employee,
                                            Project project,
                                            Integer allocationPercent,
                                            BillingType billingType) {

        // 🔴 IMPORTANT RULE: total allocation ≤ 100
        int currentAllocation = getTotalAllocation(employee.getId());

        if (currentAllocation + allocationPercent > 100) {
            throw new RuntimeException("Allocation exceeds 100%");
        }

        ProjectAssignment assignment = new ProjectAssignment();
        assignment.setEmployee(employee);
        assignment.setProject(project);
        assignment.setAllocationPercent(allocationPercent);
        assignment.setBillingType(billingType);

        return projectAssignmentRepository.save(assignment);
    }

    // Helper method
    private int getTotalAllocation(Long employeeId) {

        List<ProjectAssignment> assignments =
                projectAssignmentRepository.findByEmployeeId(employeeId);

        return assignments.stream()
                .mapToInt(ProjectAssignment::getAllocationPercent)
                .sum();
    }

    //gap analysis : method added later
    public GapAnalysisResponse getGapAnalysis(Long projectId, Long employeeId) {

        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found"));

        // Project required skills
        List<String> requiredSkills = projectSkillRepository
                .findAll()
                .stream()
                .filter(ps -> ps.getProject().getId().equals(projectId))
                .map(ps -> ps.getSkill().getName())
                .toList();

        // Employee approved skills
        List<String> employeeSkills = employeeSkillRepository
                .findByEmployeeId(employeeId)
                .stream()
                .filter(es -> es.getStatus() == SkillStatus.APPROVED)
                .map(EmployeeSkill::getSkill)
                .map(skill -> skill.getName())
                .toList();

        // Gap = required - employee
        List<String> missingSkills = requiredSkills.stream()
                .filter(skill -> !employeeSkills.contains(skill))
                .toList();

        GapAnalysisResponse response = new GapAnalysisResponse();
        response.setProjectId(project.getId());
        response.setProjectName(project.getName());
        response.setRequiredSkills(requiredSkills);
        response.setEmployeeSkills(employeeSkills);
        response.setMissingSkills(missingSkills);

        return response;
    }

}
