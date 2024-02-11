package com.goalkeepers.server.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TargetResponseDto {
    private Long targetId;
    private Integer targetPage;
    private Long commentId;
    private Integer commentPage;

    public static TargetResponseDto of(Long targetId, Integer targetPage, Long commentId, Integer commentPage) {
        return TargetResponseDto.builder()
                .targetId(targetId)
                .targetPage(targetPage)
                .commentId(commentId)
                .commentPage(commentPage)
                .build();
    }
}
