package com.skillbridge.employee.service;

import com.skillbridge.employee.entity.Employee;
import com.skillbridge.employee.repository.EmployeeRepository;
import com.skillbridge.user.entity.User;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class EmployeeService {

    private final EmployeeRepository employeeRepository;

    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    // Create employee profile for a user
    public Employee createEmployee(User user, String name, String department, String designation) {

        Employee employee = new Employee();
        employee.setUser(user);
        employee.setName(name);
        employee.setDepartment(department);
        employee.setDesignation(designation);

        return employeeRepository.save(employee);
    }

    // Fetch employee by user id
    public Optional<Employee> getEmployeeByUserId(Long userId) {
        return employeeRepository.findByUserId(userId);
    }
}
