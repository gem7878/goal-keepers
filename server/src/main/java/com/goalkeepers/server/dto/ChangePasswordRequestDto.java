package com.goalkeepers.server.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ChangePasswordRequestDto {

    @NotBlank(message = "이메일을 입력해주세요.")
    @Email(message = "올바른 이메일 형식으로 작성해주세요.")
    private String email;

    @NotBlank(message = "이전 비밀번호를 입력해주세요.")
    private String exPassword;

    @NotBlank(message = "새로운 비밀번호를 입력해주세요.")
    private String newPassword;
}
