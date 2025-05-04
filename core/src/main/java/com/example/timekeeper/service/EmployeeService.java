package com.example.timekeeper.service;

import com.example.timekeeper.entity.EmployeeEntity;
import com.example.timekeeper.repository.EmployeeRepository;
import org.springframework.stereotype.Service;

@Service
public class EmployeeService {
    private final EmployeeRepository employeeRepository;

    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    public void save(EmployeeEntity employee) {
        employeeRepository.save(employee);
    }
}
