package com.example.timekeeper.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class EmployeeResDto {
    private String name;
    private String title;
    private String email;
}
