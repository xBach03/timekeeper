package com.example.timekeeper.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.time.LocalDate;
import java.time.LocalTime;

@Setter
@Getter
@Accessors(chain = true)
public class OTLogDto {
    private String name;
    private LocalDate date;
    private LocalTime startHour;
    private LocalTime endHour;
    private String project;
}
