package com.example.timekeeper.service;

import com.example.timekeeper.entity.ManagerEntity;
import com.example.timekeeper.repository.ManagerRepository;
import org.springframework.stereotype.Service;

@Service
public class ManagerService {
    private final ManagerRepository managerRepository;

    public ManagerService(ManagerRepository managerRepository) {
        this.managerRepository = managerRepository;
    }

    public void save(ManagerEntity manager) {
        managerRepository.save(manager);
    }
}
