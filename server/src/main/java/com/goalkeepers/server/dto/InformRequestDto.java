package com.goalkeepers.server.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class InformRequestDto {

    @NotBlank(message = "제목을 입력해주세요.")
    @Size(max = 50, message = "50자 이하로 작성하시오.")
    private String title;

    @NotBlank(message = "내용을 입력해주세요.")
    private String content;
}
