package com.example.timekeeper.service;

import com.example.timekeeper.entity.PayrollEntity;
import com.example.timekeeper.repository.PayrollRepository;
import org.springframework.stereotype.Service;

@Service
public class PayrollService {
    private final PayrollRepository payrollRepository;

    public PayrollService(PayrollRepository payrollRepository) {
        this.payrollRepository = payrollRepository;
    }

    public void save(PayrollEntity payroll) {
        payrollRepository.save(payroll);
    }
}
