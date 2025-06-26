package com.example.timekeeper.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.time.LocalDate;

@Setter
@Getter
@Accessors(chain = true)
public class OvertimeDto {
    private LocalDate date;
    private String startHour;
    private String endHour;
    private float totalHour;
    private String project;
    private String approval;
}
