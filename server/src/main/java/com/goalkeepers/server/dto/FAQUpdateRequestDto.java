package com.goalkeepers.server.dto;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FAQUpdateRequestDto {

    @Size(max = 50, message = "50자 이하로 입력해주세요")
    private String title;

    private String content;
}
