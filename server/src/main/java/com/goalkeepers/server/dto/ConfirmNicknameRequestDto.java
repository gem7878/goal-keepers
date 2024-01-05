package com.goalkeepers.server.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ConfirmNicknameRequestDto {

    @NotBlank(message = "닉네임을 입력해주세요.")
    @Size(min = 3, max = 15, message = "닉네임은 3 ~ 15자 입니다.")
    private String nickname;
}
