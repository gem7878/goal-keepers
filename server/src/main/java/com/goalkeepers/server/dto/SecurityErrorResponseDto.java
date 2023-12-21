package com.goalkeepers.server.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SecurityErrorResponseDto {
    private String message;
    private int status;

    public static SecurityErrorResponseDto of(String message, int status) {
        return SecurityErrorResponseDto.builder()
                .message(message)
                .status(status)
                .build();
    }
}
