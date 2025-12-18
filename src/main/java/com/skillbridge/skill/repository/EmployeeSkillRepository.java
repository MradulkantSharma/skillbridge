package com.skillbridge.skill.repository;

import com.skillbridge.common.enums.SkillStatus;
import com.skillbridge.skill.entity.EmployeeSkill;
import org.springframework.data.jpa.repository.JpaRepository;
import com.skillbridge.skill.repository.EmployeeSkillRepository;

import java.util.List;

public interface EmployeeSkillRepository extends JpaRepository<EmployeeSkill, Long> {

    List<EmployeeSkill> findByEmployeeId(Long employeeId);
    List<EmployeeSkill> findBySkill_NameAndStatus(
            String skillName,
            SkillStatus status
    );

}
