package com.goalkeepers.server.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PostContentUpdateRequestDto {

    @NotBlank(message = "내용을 입력해주세요.")
    private String content;
}
