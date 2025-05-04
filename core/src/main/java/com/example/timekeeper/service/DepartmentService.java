package com.example.timekeeper.service;

import com.example.timekeeper.entity.DepartmentEntity;
import com.example.timekeeper.repository.DepartmentRepository;
import org.springframework.stereotype.Service;

@Service
public class DepartmentService {
    private final DepartmentRepository departmentRepository;

    public DepartmentService(DepartmentRepository departmentRepository) {
        this.departmentRepository = departmentRepository;
    }

    public void save(DepartmentEntity department) {
        departmentRepository.save(department);
    }
}
