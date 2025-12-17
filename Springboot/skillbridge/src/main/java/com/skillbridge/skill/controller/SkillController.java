package com.skillbridge.skill.controller;

import com.skillbridge.common.enums.SkillStatus;
import com.skillbridge.employee.entity.Employee;
import com.skillbridge.employee.repository.EmployeeRepository;
import com.skillbridge.skill.entity.EmployeeSkill;
import com.skillbridge.skill.service.SkillService;
import com.skillbridge.user.entity.User;
import com.skillbridge.user.repository.UserRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/skills")
public class SkillController {

    private final SkillService skillService;
    private final EmployeeRepository employeeRepository;
    private final UserRepository userRepository;

    public SkillController(SkillService skillService,
                           EmployeeRepository employeeRepository,
                           UserRepository userRepository) {
        this.skillService = skillService;
        this.employeeRepository = employeeRepository;
        this.userRepository = userRepository;
    }

    // Employee adds skill
    @PostMapping("/add")
    public EmployeeSkill addSkill(@RequestParam Long employeeId,
                                  @RequestParam String skillName) {

        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        return skillService.addSkill(employee, skillName);
    }

    // Leader / Superuser reviews skill
    @PutMapping("/review")
    public EmployeeSkill reviewSkill(@RequestParam Long employeeSkillId,
                                     @RequestParam SkillStatus status,
                                     @RequestParam Long reviewerUserId) {

        User reviewer = userRepository.findById(reviewerUserId)
                .orElseThrow(() -> new RuntimeException("Reviewer not found"));

        return skillService.reviewSkill(employeeSkillId, status, reviewer);
    }

    // Get all skills of employee
    @GetMapping("/employee/{employeeId}")
    public List<EmployeeSkill> getEmployeeSkills(@PathVariable Long employeeId) {
        return skillService.getSkillsOfEmployee(employeeId);
    }
}
