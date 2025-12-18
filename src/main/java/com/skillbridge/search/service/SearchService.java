package com.skillbridge.search.service;

import com.skillbridge.common.enums.SkillStatus;
import com.skillbridge.employee.entity.Employee;
import com.skillbridge.employee.repository.EmployeeRepository;
import com.skillbridge.project.entity.ProjectAssignment;
import com.skillbridge.project.repository.ProjectAssignmentRepository;
import com.skillbridge.search.dto.EmployeeSearchResponse;
import com.skillbridge.skill.entity.EmployeeSkill;
import com.skillbridge.skill.repository.EmployeeSkillRepository;
import com.skillbridge.user.entity.User;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

    public List<EmployeeSearchResponse> searchEmployeesBySkill(String skillName) {

        List<EmployeeSkill> employeeSkills =
                employeeSkillRepository.findBySkill_NameAndStatus(
                        skillName,
                        SkillStatus.APPROVED
                );

        Map<Long, EmployeeSearchResponse> result = new HashMap<>();

        for (EmployeeSkill es : employeeSkills) {
            Employee employee = es.getEmployee();

            EmployeeSearchResponse response =
                    result.computeIfAbsent(employee.getId(), id -> {
                        EmployeeSearchResponse r = new EmployeeSearchResponse();
                        r.setEmployeeId(employee.getId());
                        r.setName(employee.getName());
                        r.setDepartment(employee.getDepartment());
                        r.setDesignation(employee.getDesignation());
                        r.setApprovedSkills(new ArrayList<>());
                        return r;
                    });

            response.getApprovedSkills().add(es.getSkill().getName());
        }

        return new ArrayList<>(result.values());
    }
    public EmployeeSearchResponse getEmployeeAvailability(Long employeeId) {

        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        List<ProjectAssignment> assignments =
                        projectAssignmentRepository.findByEmployee(employee);


        int totalAllocation = assignments.stream()
                .mapToInt(ProjectAssignment::getAllocationPercent)
                .sum();

        EmployeeSearchResponse response = new EmployeeSearchResponse();
        response.setEmployeeId(employee.getId());
        response.setName(employee.getName());
        response.setDepartment(employee.getDepartment());
        response.setDesignation(employee.getDesignation());
        response.setTotalAllocation(totalAllocation);

        List<EmployeeSearchResponse.ProjectInfo> projectInfos = assignments.stream()
                .map(pa -> {
                    EmployeeSearchResponse.ProjectInfo pi =
                            new EmployeeSearchResponse.ProjectInfo();
                    pi.projectId = pa.getProject().getId();
                    pi.projectName = pa.getProject().getName();
                    pi.allocationPercent = pa.getAllocationPercent();
                    pi.billingType = pa.getBillingType().name();
                    return pi;
                })
                .toList();

        response.setProjects(projectInfos);

        return response;
    }



}
