package com.goalkeepers.server.dto;

import java.util.List;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class DeleteAlarmRequestDto {
    
    @NotNull(message = "전체이면 true, 부분이면 false 값을 입력해주세요.")
    private Boolean all;

    private List<Long> deleteList;
}
