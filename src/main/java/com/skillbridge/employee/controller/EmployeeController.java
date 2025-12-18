package com.skillbridge.employee.controller;

import com.skillbridge.employee.entity.Employee;
import com.skillbridge.employee.repository.EmployeeRepository;
import com.skillbridge.employee.service.EmployeeService;
import com.skillbridge.user.entity.User;
import com.skillbridge.user.repository.UserRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/employees")
public class EmployeeController {

    private final EmployeeService employeeService;
    private final UserRepository userRepository;


    public EmployeeController(EmployeeService employeeService,
                              UserRepository userRepository) {
        this.employeeService = employeeService;
        this.userRepository = userRepository;
    }

    // Create employee profile for existing user
    @PostMapping
    public Employee createEmployee(@RequestParam Long userId,
                                   @RequestParam String name,
                                   @RequestParam String department,
                                   @RequestParam String designation) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return employeeService.createEmployee(user, name, department, designation);
    }

    @GetMapping
    public List<Employee> getAllEmployees() {
        return employeeService.getAllEmployees();
    }


}
