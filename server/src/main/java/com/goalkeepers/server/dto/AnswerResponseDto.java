package com.goalkeepers.server.dto;

import com.goalkeepers.server.entity.Answer;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AnswerResponseDto {
    private String answer;

    public static AnswerResponseDto of(Answer answer) {
        return AnswerResponseDto.builder()
                .answer(answer.getContent())
                .build();
    }
}
