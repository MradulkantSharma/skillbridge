package com.skillbridge.project.controller;

import com.skillbridge.common.enums.BillingType;
import com.skillbridge.common.enums.ProjectStatus;
import com.skillbridge.common.util.SecurityUtil;
import com.skillbridge.employee.entity.Employee;
import com.skillbridge.employee.repository.EmployeeRepository;
import com.skillbridge.project.dto.AssignEmployeeRequest;
import com.skillbridge.project.dto.CreateProjectRequest;
import com.skillbridge.project.entity.Project;
import com.skillbridge.project.entity.ProjectAssignment;
import com.skillbridge.project.entity.ProjectSkill;
import com.skillbridge.project.repository.ProjectRepository;
import com.skillbridge.project.service.ProjectService;
import com.skillbridge.user.entity.User;
import com.skillbridge.user.repository.UserRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import com.skillbridge.project.dto.GapAnalysisResponse;


import java.util.List;

@RestController
@RequestMapping("/api/projects")
public class ProjectController {

    private final ProjectService projectService;
    private final UserRepository userRepository;
    private final EmployeeRepository employeeRepository;
    private final ProjectRepository projectRepository;

    public ProjectController(ProjectService projectService,
                             UserRepository userRepository,
                             EmployeeRepository employeeRepository,
                             ProjectRepository projectRepository) {
        this.projectService = projectService;
        this.userRepository = userRepository;
        this.employeeRepository = employeeRepository;
        this.projectRepository = projectRepository;
    }
    @PreAuthorize("hasAnyRole('SUPERUSER','HR')")
    @PostMapping
    public Project createProject(@RequestBody CreateProjectRequest request) {

        String email = SecurityUtil.getCurrentUserEmail();

        User creator = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return projectService.createProject(
                request.name,
                request.description,
                request.status,
                creator
        );
    }



    // 2️⃣ Add required skill to project
    @PreAuthorize("hasAnyRole('SUPERUSER','HR')")
    @PostMapping("/{projectId}/skills")
    public ProjectSkill addRequiredSkill(@PathVariable Long projectId,
                                         @RequestParam String skillName) {

        return projectService.addRequiredSkill(projectId, skillName);
    }

    // 3️⃣ Assign employee to project
    @PreAuthorize("hasAnyRole('SUPERUSER','HR')")
    @PostMapping("/{projectId}/assign")
    public ProjectAssignment assignEmployee(@PathVariable Long projectId,
                                            @RequestBody AssignEmployeeRequest request) {

        Employee employee = employeeRepository.findById(request.employeeId)
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found"));

        return projectService.assignEmployee(
                employee,
                project,
                request.allocationPercent,
                request.billingType
        );
    }


    // 4️⃣ View all projects
    @GetMapping
    public List<Project> getAllProjects() {
        return projectRepository.findAll();
    }

    // 5️⃣ Gap Analysis
    @GetMapping("/{projectId}/gap/{employeeId}")
    public GapAnalysisResponse getGapAnalysis(@PathVariable Long projectId,
                                              @PathVariable Long employeeId) {

        return projectService.getGapAnalysis(projectId, employeeId);
    }

}
