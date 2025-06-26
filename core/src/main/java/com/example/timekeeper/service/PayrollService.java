package com.example.timekeeper.service;

import com.example.timekeeper.dto.PayrollDto;
import com.example.timekeeper.entity.PayrollEntity;
import com.example.timekeeper.repository.PayrollRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class PayrollService {
    private final PayrollRepository payrollRepository;

    public PayrollService(PayrollRepository payrollRepository) {
        this.payrollRepository = payrollRepository;
    }

    public void save(PayrollEntity payroll) {
        payrollRepository.save(payroll);
    }

    public List<PayrollDto> getPayroll(String username) {
        LocalDate now = LocalDate.now();
        LocalDate start = now.withDayOfMonth(1);
        LocalDate end = now.withDayOfMonth(now.lengthOfMonth());
        return payrollRepository.getByEmployeeNameAndDate(username, start, end)
                .stream()
                .map(pe -> new PayrollDto().setDate(pe.getDate())
                        .setHourlyRate(pe.getHourlyRate())
                        .setHoursWorked(pe.getHoursWorked())
                        .setBonus(pe.getBonus())
                        .setTax(pe.getTax())
                        .setTotalPay(pe.getTotalSalary()))
                .toList();
    }
}
