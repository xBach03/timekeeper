package com.example.timekeeper.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Setter
@Getter
@Accessors(chain = true)
@AllArgsConstructor
public class LeaveStatusChartDto {
    private String name;
    private long value;
}
