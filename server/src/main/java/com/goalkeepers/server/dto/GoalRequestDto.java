package com.goalkeepers.server.dto;

import java.time.LocalDate;

import com.goalkeepers.server.entity.Goal;
import com.goalkeepers.server.entity.Member;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GoalRequestDto {
    
    @NotBlank(message = "제목을 입력해주세요.")
    private String title;

    @NotBlank(message = "설명을 입력해주세요.")
    private String description;

    @NotNull(message = "시작 날짜를 입력해주세요.")
    private LocalDate startDate;

    @NotNull(message = "끝나는 날짜를 입력해주세요.")
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
