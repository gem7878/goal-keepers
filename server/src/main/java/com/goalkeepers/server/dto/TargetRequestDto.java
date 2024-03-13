package com.goalkeepers.server.dto;

import com.goalkeepers.server.entity.TYPE;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TargetRequestDto {
    private TYPE type;
    private long targetId;
    private long commentId;
}
