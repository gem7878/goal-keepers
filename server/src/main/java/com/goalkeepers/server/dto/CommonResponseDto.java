package com.goalkeepers.server.dto;

import java.time.LocalDateTime;
import lombok.Getter;

@Getter
public class CommonResponseDto {
    private final LocalDateTime timestamp = LocalDateTime.now();
    private boolean success;
    private String message;
    private Object data;

    public CommonResponseDto(boolean success, String message, Object data) {
        this.success = success;
        this.message = message;
        this.data = data;
    }

    public CommonResponseDto(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public CommonResponseDto(boolean success, Object data) {
        this.success = success;
        this.data = data;
    }
}
