package com.goalkeepers.server.form;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserRegisterForm {
    
    @NotEmpty(message = "이메일은 필수항목입니다.")
    @Email(message = "올바른 이메일 주소를 입력하세요.")
    private String email;

    @Size(min = 3, max = 25, message = "비밀번호는 3자 이상 25자 이하입니다.")
    @NotEmpty(message = "비밀번호는 필수항목입니다.")
    private String password;

    @NotEmpty(message = "비밀번호 확인은 필수항목입니다.")
    private String confirmPassword;

    @Size(min = 3, max = 10, message = "닉네임은 3자 이상 10자 이하입니다.")
    @NotEmpty(message = "닉네임은 필수항목입니다.")
    private String nickName;

    
}
