package com.goalkeepers.server.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.goalkeepers.server.dto.ChangePasswordRequestDto;
import com.goalkeepers.server.dto.MemberRequestDto;
import com.goalkeepers.server.dto.MemberResponseDto;
import com.goalkeepers.server.service.MemberService;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {
    
    private final MemberService memberService;

    @GetMapping("/me")
    public ResponseEntity<MemberResponseDto> getMyMemberInfo() {
        MemberResponseDto myInfoBySecurity = memberService.getMyInfoBySecurity();
        return ResponseEntity.ok(myInfoBySecurity);
    }

    @PostMapping("/nickname")
    public ResponseEntity<MemberResponseDto> setMemberNickname(@RequestBody MemberRequestDto requestDto) {
        return ResponseEntity.ok(memberService.changeMemberNickname(requestDto.getNickname()));
    }
    
    @PostMapping("/password")
    public ResponseEntity<MemberResponseDto> setMemberPassword(@RequestBody ChangePasswordRequestDto requestDto) {
        return ResponseEntity.ok(memberService.changeMemberPassword(requestDto.getEmail(), requestDto.getExPassword(), requestDto.getNewPassword()));
    }
}
