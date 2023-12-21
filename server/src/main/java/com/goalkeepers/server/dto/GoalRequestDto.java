package com.goalkeepers.server.dto;

import java.time.LocalDate;

import com.goalkeepers.server.entity.Goal;
import com.goalkeepers.server.entity.Member;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GoalRequestDto {
    
    private String title;
    private String description;
    private LocalDate startDate;
    private LocalDate endDate;
    private String imageUrl;

    public Goal toGoal(Member member) {
        return Goal.builder()
                .title(title)
                .description(description)
                .startDate(startDate)
                .endDate(endDate)
                .imageUrl(imageUrl)
                .member(member)
                .build();
    }
}
