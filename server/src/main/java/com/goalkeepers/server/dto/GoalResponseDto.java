package com.goalkeepers.server.dto;

import java.time.LocalDate;

import com.goalkeepers.server.entity.Goal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GoalResponseDto {
    private Long goalId;
    private String title;
    private String description;
    private LocalDate startDate;
    private LocalDate endDate;
    private String imageUrl;
    private int shareCnt;
    private Boolean isShare;

    public static GoalResponseDto of(Goal goal, Boolean isShare) {
        return GoalResponseDto.builder()
                .goalId(goal.getId())
                .title(goal.getTitle())
                .description(goal.getDescription())
                .startDate(goal.getStartDate())
                .endDate(goal.getEndDate())
                .imageUrl(goal.getImageUrl())
                .shareCnt(goal.getShareCnt())
                .isShare(isShare)
                .build();
    }

    public static GoalResponseDto of(Goal goal) {
        return of(goal, false);
    }
}
