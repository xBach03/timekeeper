package com.example.timekeeper.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class EmployeeReqDto {
    private String name;
    private String email;
    private Date dateOfBirth;
    private String phoneNumber;
    private String title;
}
