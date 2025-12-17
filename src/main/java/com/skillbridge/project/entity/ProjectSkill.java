package com.skillbridge.project.entity;

import com.skillbridge.skill.entity.Skill;
import jakarta.persistence.*;

@Entity
@Table(name = "project_skills")
public class ProjectSkill {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;

    @ManyToOne
    @JoinColumn(name = "skill_id", nullable = false)
    private Skill skill;

    // -------- Getters & Setters --------

    public Long getId() {
        return id;
    }

    public Project getProject() {
        return project;
    }

    public Skill getSkill() {
        return skill;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public void setSkill(Skill skill) {
        this.skill = skill;
    }
}
