package com.example.timekeeper.service;

import com.example.timekeeper.dto.EmployeeResDto;
import com.example.timekeeper.entity.EmployeeEntity;
import com.example.timekeeper.repository.EmployeeRepository;
import org.springframework.stereotype.Service;

@Service
public class EmployeeService {
    private final EmployeeRepository employeeRepository;

    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    public EmployeeResDto save(EmployeeEntity employee) {
        EmployeeEntity saved = employeeRepository.save(employee);
        return new EmployeeResDto(
                saved.getName(),
                saved.getTitle(),
                saved.getEmail()
        );
    }
}
