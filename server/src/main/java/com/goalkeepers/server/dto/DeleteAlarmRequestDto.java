package com.goalkeepers.server.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class DeleteAlarmRequestDto {
    private boolean all;
    private List<Long> deleteList;
}
