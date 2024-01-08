package com.goalkeepers.server.dto;

import java.util.Objects;
import java.time.LocalDate;

import com.goalkeepers.server.entity.Goal;
import com.goalkeepers.server.entity.Member;

import jakarta.validation.constraints.NotBlank;
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

    // @NotBlank(message = "설명을 입력해주세요.")
    private String description;

    // @NotNull(message = "시작 날짜를 입력해주세요.")
    private LocalDate startDate;

    // @NotNull(message = "끝나는 날짜를 입력해주세요.")
    private LocalDate endDate;

    public Goal toGoal(Member member, String imageUrl) {
        return Goal.builder()
                .title(title)
                .description(Objects.isNull(description) ? "" : description)
                .startDate(Objects.isNull(startDate) ? LocalDate.now() : startDate)
                .endDate(Objects.isNull(endDate) ? LocalDate.now().plusYears(1) : endDate)
                .imageUrl(imageUrl)
                .member(member)
                .build();
    }
}
