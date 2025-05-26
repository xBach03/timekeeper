package com.example.timekeeper.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.OffsetDateTime;

@Setter
@Getter
public class TimeCheckDto {
    private OffsetDateTime time;
    private String name;
}
