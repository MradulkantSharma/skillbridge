package com.skillbridge.skill.service;

import com.skillbridge.common.enums.SkillStatus;
import com.skillbridge.employee.entity.Employee;
import com.skillbridge.skill.entity.EmployeeSkill;
import com.skillbridge.skill.entity.Skill;
import com.skillbridge.skill.repository.EmployeeSkillRepository;
import com.skillbridge.skill.repository.SkillRepository;
import com.skillbridge.user.entity.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SkillService {

    private final SkillRepository skillRepository;
    private final EmployeeSkillRepository employeeSkillRepository;

    public SkillService(SkillRepository skillRepository,
                        EmployeeSkillRepository employeeSkillRepository) {
        this.skillRepository = skillRepository;
        this.employeeSkillRepository = employeeSkillRepository;
    }

    // Employee adds a skill
    public EmployeeSkill addSkill(Employee employee, String skillName) {

        // find or create skill
        Skill skill = skillRepository.findByName(skillName)
                .orElseGet(() -> {
                    Skill newSkill = new Skill();
                    newSkill.setName(skillName);
                    return skillRepository.save(newSkill);
                });

        EmployeeSkill employeeSkill = new EmployeeSkill();
        employeeSkill.setEmployee(employee);
        employeeSkill.setSkill(skill);
        employeeSkill.setStatus(SkillStatus.PENDING);

        return employeeSkillRepository.save(employeeSkill);
    }

    // Leader/Superuser reviews skill
    public EmployeeSkill reviewSkill(Long employeeSkillId,
                                     SkillStatus status,
                                     User reviewer) {

        EmployeeSkill employeeSkill = employeeSkillRepository
                .findById(employeeSkillId)
                .orElseThrow(() -> new RuntimeException("Employee skill not found"));

        employeeSkill.setStatus(status);
        employeeSkill.setReviewedBy(reviewer);

        return employeeSkillRepository.save(employeeSkill);
    }

    // Get all skills of an employee
    public List<EmployeeSkill> getSkillsOfEmployee(Long employeeId) {
        return employeeSkillRepository.findByEmployeeId(employeeId);
    }
}
