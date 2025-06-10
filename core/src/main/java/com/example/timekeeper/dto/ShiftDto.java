package com.example.timekeeper.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.time.LocalDate;

@Setter
@Getter
@Accessors(chain = true)
@ToString
public class ShiftDto {
    private LocalDate date;
    private String time;
    private String location;
}
