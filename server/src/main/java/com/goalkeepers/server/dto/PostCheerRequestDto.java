package com.goalkeepers.server.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PostCheerRequestDto {
    @NotNull(message = "post id 값이 필요합니다.")
    private Long postId;
}
