package com.example.timekeeper.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.List;

@Setter
@Getter
@Accessors(chain = true)
public class DashBoardResDto {
    private String shift;
    private String breakTime;
    private String leaveRequests;
    private List<String> notifications;

}
