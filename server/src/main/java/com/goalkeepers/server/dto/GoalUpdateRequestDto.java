package com.goalkeepers.server.dto;

import java.time.LocalDate;

import lombok.Getter;

@Getter
public class GoalUpdateRequestDto {
    private String title;

    private String description;

    private LocalDate startDate;

    private LocalDate endDate;
}
