package com.goalkeepers.server.controller;

import org.springframework.web.bind.annotation.RestController;

import com.goalkeepers.server.dto.ConfirmEmailRequestDto;
import com.goalkeepers.server.dto.ConfirmNicknameRequestDto;
import com.goalkeepers.server.dto.MemberRequestDto;
import com.goalkeepers.server.dto.MemberResponseDto;
import com.goalkeepers.server.dto.TokenDto;
import com.goalkeepers.server.service.AuthService;

import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.RequestMapping;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;



@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    
    private final AuthService authService;
    
    @PostMapping("/signup")
    public ResponseEntity<MemberResponseDto> signup(@RequestBody MemberRequestDto requestDto) {
        return ResponseEntity.ok(authService.signup(requestDto));
    }

    @PostMapping("/login")
    public ResponseEntity<TokenDto> login(@RequestBody MemberRequestDto requestDto) {
        return ResponseEntity.ok(authService.login(requestDto));
    }

    @PostMapping("/email")
    public ResponseEntity<Map<String, Object>> emailConfirm(@RequestBody ConfirmEmailRequestDto requestDto) {
        return ResponseEntity.ok(authService.confirmDuplicateEmail(requestDto.getEmail()));
    }

    @PostMapping("/nickname")
    public ResponseEntity<Map<String, Object>> nicknameConfirm(@RequestBody ConfirmNicknameRequestDto requestDto) {
        return ResponseEntity.ok(authService.confirmDuplicateNickname(requestDto.getNickname()));
    }
    
}
