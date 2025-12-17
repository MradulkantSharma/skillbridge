package com.skillbridge.skill.entity;

import com.skillbridge.common.enums.SkillStatus;
import com.skillbridge.employee.entity.Employee;
import com.skillbridge.user.entity.User;
import jakarta.persistence.*;

@Entity
@Table(name = "employee_skills")
public class EmployeeSkill {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;

    @ManyToOne
    @JoinColumn(name = "skill_id", nullable = false)
    private Skill skill;

    @Enumerated(EnumType.STRING)
    private SkillStatus status = SkillStatus.PENDING;

    // Who reviewed (Leader or Superuser)
    @ManyToOne
    @JoinColumn(name = "reviewed_by")
    private User reviewedBy;

    // -------- Getters & Setters --------

    public Long getId() {
        return id;
    }

    public Employee getEmployee() {
        return employee;
    }

    public Skill getSkill() {
        return skill;
    }

    public SkillStatus getStatus() {
        return status;
    }

    public User getReviewedBy() {
        return reviewedBy;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public void setSkill(Skill skill) {
        this.skill = skill;
    }

    public void setStatus(SkillStatus status) {
        this.status = status;
    }

    public void setReviewedBy(User reviewedBy) {
        this.reviewedBy = reviewedBy;
    }
}
