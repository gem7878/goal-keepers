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

import org.springframework.http.ResponseEntity;
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

    @PostMapping("/email")
    public ResponseEntity<CommonResponseDto> emailConfirm(@Valid @RequestBody ConfirmEmailRequestDto requestDto) {
        authService.confirmDuplicateEmail(requestDto.getEmail());
        return ResponseEntity.ok(new CommonResponseDto(true, "사용가능한 이메일입니다."));
    }

    @PostMapping("/nickname")
    public ResponseEntity<CommonResponseDto> nicknameConfirm(@Valid @RequestBody ConfirmNicknameRequestDto requestDto) {
        authService.confirmDuplicateNickname(requestDto.getNickname());
        return ResponseEntity.ok(new CommonResponseDto(true, "사용가능한 닉네임입니다."));
    }
    
}
