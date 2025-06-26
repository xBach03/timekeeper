package com.example.timekeeper.service;

import com.example.timekeeper.constant.Status;
import com.example.timekeeper.dto.LoginResDto;
import com.example.timekeeper.entity.EmployeeSessionEntity;
import com.example.timekeeper.repository.EmployeeSessionRepository;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class EmployeeSessionService {

    private final EmployeeSessionRepository employeeSessionRepository;

    public EmployeeSessionService(EmployeeSessionRepository employeeSessionRepository) {
        this.employeeSessionRepository = employeeSessionRepository;
    }

    public void save(EmployeeSessionEntity employeeSessionEntity) {
        employeeSessionRepository.save(employeeSessionEntity);
    }

    public LoginResDto login(String name) {
        EmployeeSessionEntity currentSession = employeeSessionRepository.findByEmployeeName(name);
        if (Objects.nonNull(currentSession)) {
            currentSession.setStatus(Status.ACTIVE);
            employeeSessionRepository.save(currentSession);
            return new LoginResDto()
                    .setName(name)
                    .setMessage("Login success");
        }
        return null;
    }

    public String logout(String name) {
        EmployeeSessionEntity currentSession = employeeSessionRepository.findByEmployeeName(name);
        currentSession.setStatus(Status.INACTIVE);
        employeeSessionRepository.save(currentSession);
        return "Sucess";
    }
}
