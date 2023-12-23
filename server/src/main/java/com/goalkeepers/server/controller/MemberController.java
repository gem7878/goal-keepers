package com.goalkeepers.server.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.goalkeepers.server.dto.ChangePasswordRequestDto;
import com.goalkeepers.server.dto.CommonResponseDto;
import com.goalkeepers.server.dto.ConfirmNicknameRequestDto;
import com.goalkeepers.server.dto.MemberResponseDto;
import com.goalkeepers.server.service.MemberService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {
    
    private final MemberService memberService;

    @GetMapping("/me")
    public ResponseEntity<CommonResponseDto> getMyMemberInfo() {
        MemberResponseDto myInfoBySecurity = memberService.getMyInfoBySecurity();
        return ResponseEntity.ok(new CommonResponseDto(true, myInfoBySecurity));
    }

    @PostMapping("/nickname")
    public ResponseEntity<CommonResponseDto> setMemberNickname(@Valid @RequestBody ConfirmNicknameRequestDto requestDto) {
        return ResponseEntity.ok(new CommonResponseDto(true, memberService.changeMemberNickname(requestDto.getNickname())));
    }
    
    @PostMapping("/password")
    public ResponseEntity<CommonResponseDto> setMemberPassword(@Valid @RequestBody ChangePasswordRequestDto requestDto) {
        return ResponseEntity.ok(new CommonResponseDto(true, memberService.changeMemberPassword(requestDto.getEmail(), requestDto.getExPassword(), requestDto.getNewPassword())));
    }
}
