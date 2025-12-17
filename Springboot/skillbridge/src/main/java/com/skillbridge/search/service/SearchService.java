package com.skillbridge.search.service;

import com.skillbridge.common.enums.SkillStatus;
import com.skillbridge.employee.entity.Employee;
import com.skillbridge.employee.repository.EmployeeRepository;
import com.skillbridge.project.entity.ProjectAssignment;
import com.skillbridge.project.repository.ProjectAssignmentRepository;
import com.skillbridge.search.dto.EmployeeSearchResponse;
import com.skillbridge.skill.entity.EmployeeSkill;
import com.skillbridge.skill.repository.EmployeeSkillRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SearchService {

    private final EmployeeRepository employeeRepository;
    private final EmployeeSkillRepository employeeSkillRepository;
    private final ProjectAssignmentRepository projectAssignmentRepository;

    public SearchService(EmployeeRepository employeeRepository,
                         EmployeeSkillRepository employeeSkillRepository,
                         ProjectAssignmentRepository projectAssignmentRepository) {
        this.employeeRepository = employeeRepository;
        this.employeeSkillRepository = employeeSkillRepository;
        this.projectAssignmentRepository = projectAssignmentRepository;
    }

    public EmployeeSearchResponse getEmployeeDetails(Long employeeId) {

        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        // Approved skills only
        List<String> approvedSkills = employeeSkillRepository
                .findByEmployeeId(employeeId)
                .stream()
                .filter(es -> es.getStatus() == SkillStatus.APPROVED)
                .map(es -> es.getSkill().getName())
                .collect(Collectors.toList());

        // Project assignments
        List<ProjectAssignment> assignments =
                projectAssignmentRepository.findByEmployeeId(employeeId);

        int totalAllocation = assignments.stream()
                .mapToInt(ProjectAssignment::getAllocationPercent)
                .sum();

        List<EmployeeSearchResponse.ProjectInfo> projects =
                assignments.stream().map(pa -> {
                    EmployeeSearchResponse.ProjectInfo info =
                            new EmployeeSearchResponse.ProjectInfo();
                    info.projectId = pa.getProject().getId();
                    info.projectName = pa.getProject().getName();
                    info.allocationPercent = pa.getAllocationPercent();
                    info.billingType = pa.getBillingType().name();
                    return info;
                }).collect(Collectors.toList());

        // Build response
        EmployeeSearchResponse response = new EmployeeSearchResponse();
        response.setEmployeeId(employee.getId());
        response.setName(employee.getName());
        response.setDepartment(employee.getDepartment());
        response.setDesignation(employee.getDesignation());
        response.setApprovedSkills(approvedSkills);
        response.setTotalAllocation(totalAllocation);
        response.setProjects(projects);

        return response;
    }
}
