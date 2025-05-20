package com.example.timekeeper.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class RegisterResDto {
    String name;
    String message;
    String status;
}
