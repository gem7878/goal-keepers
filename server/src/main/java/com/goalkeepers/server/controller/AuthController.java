package com.goalkeepers.server.controller;

import org.springframework.web.bind.annotation.RestController;

import com.goalkeepers.server.dto.CommonResponseDto;
import com.goalkeepers.server.dto.ConfirmEmailRequestDto;
import com.goalkeepers.server.dto.ConfirmNicknameRequestDto;
import com.goalkeepers.server.dto.LoginRequestDto;
import com.goalkeepers.server.dto.MemberRequestDto;
import com.goalkeepers.server.dto.MemberResponseDto;
import com.goalkeepers.server.dto.TokenDto;
import com.goalkeepers.server.service.AuthService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;



@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    
    private final AuthService authService;
    
    @PostMapping("/signup")
    public ResponseEntity<?> signup(@Valid @RequestBody MemberRequestDto requestDto) {
        MemberResponseDto response = authService.signup(requestDto);
        return ResponseEntity.ok(new CommonResponseDto(true, response));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequestDto requestDto) {
        TokenDto response = authService.login(requestDto);
        return ResponseEntity.ok(new CommonResponseDto(true, response));
    }

    @PostMapping("/nickname")
    public ResponseEntity<CommonResponseDto> confirmNickname(@Valid @RequestBody ConfirmNicknameRequestDto requestDto) {
        authService.confirmDuplicateNickname(requestDto.getNickname());
        return ResponseEntity.ok(new CommonResponseDto(true, "사용가능한 닉네임입니다."));
    }

    @PostMapping("/email")
    public ResponseEntity<CommonResponseDto> confirmEmail(@Valid @RequestBody ConfirmEmailRequestDto requestDto) {
        authService.confirmDuplicateEmail(requestDto.getEmail());
        return ResponseEntity.ok(new CommonResponseDto(true, "사용가능한 이메일입니다."));
    }

    @PostMapping("/email/verification-request")
    public ResponseEntity<CommonResponseDto> verificationEmail(@Valid @RequestBody ConfirmEmailRequestDto requestDto) {
        authService.confirmDuplicateEmail(requestDto.getEmail());
        authService.sendCodeToEmail(requestDto.getEmail());
        return ResponseEntity.ok(new CommonResponseDto(true, "이메일을 전송하였습니다."));
    }

    @GetMapping("/email/verification")
    public ResponseEntity<CommonResponseDto> verificationCode(@RequestParam(name = "email") String email, 
                                                                @RequestParam(name = "code") String code) {
        return ResponseEntity.ok(new CommonResponseDto(authService.verifiedCode(email, code), "인증되었습니다."));
    }

    // 비밀번호 찾기
    @PostMapping("/password/find")
    public ResponseEntity<CommonResponseDto> findPassword(@Valid @RequestBody ConfirmEmailRequestDto requestDto) {
        authService.sendPasswordToEmail(requestDto.getEmail());
        return ResponseEntity.ok(new CommonResponseDto(true, "이메일을 전송하였습니다."));
    }
}
