package com.skillbridge.project.repository;

import com.skillbridge.employee.entity.Employee;
import com.skillbridge.project.entity.ProjectAssignment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProjectAssignmentRepository extends JpaRepository<ProjectAssignment, Long> {

    List<ProjectAssignment> findByEmployeeId(Long employeeId);
    List<ProjectAssignment> findByEmployee(Employee employee);

}
