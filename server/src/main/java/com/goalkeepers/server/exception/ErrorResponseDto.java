package com.goalkeepers.server.exception;

import java.time.LocalDateTime;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
public class ErrorResponseDto {
    private final LocalDateTime timestamp = LocalDateTime.now();
    private String message;
    private int status;
    private List<FieldError> validation;
    private String code;

    @Getter
    @Setter
    @NoArgsConstructor
    public static class FieldError {
        private String field;
        private String message;
    }

    public ErrorResponseDto(ErrorCode errorCode) {
        this.message = errorCode.getMessage();
        this.status = errorCode.getStatus();
        this.code = errorCode.getCode();
    }

    public ErrorResponseDto(ErrorCode errorCode, List<FieldError> errors) {
        this.message = errorCode.getMessage();
        this.status = errorCode.getStatus();
        this.code = errorCode.getCode();
        this.validation = errors;
    }

    public ErrorResponseDto(String message, int status) {
        this.message = message;
        this.status = status;
    }
}
