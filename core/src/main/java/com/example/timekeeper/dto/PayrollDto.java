package com.example.timekeeper.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.time.LocalDate;

@Setter
@Getter
@Accessors(chain = true)
public class PayrollDto {
    private LocalDate date;
    private float hoursWorked;
    private float hourlyRate;
    private float totalPay;
    private float tax;
    private float bonus;
}
