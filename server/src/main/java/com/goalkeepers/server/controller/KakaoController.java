package com.goalkeepers.server.controller;

import java.io.IOException;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.goalkeepers.server.dto.CommonResponseDto;
import com.goalkeepers.server.dto.KakaoTokenDto;
import com.goalkeepers.server.dto.TokenDto;
import com.goalkeepers.server.service.KakaoService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/kakao")
public class KakaoController {

    private final KakaoService kakaoService;

    @GetMapping("/login")
    public ResponseEntity<CommonResponseDto> kakaoCallback(@RequestParam(name = "code") String code) throws IOException {
        KakaoTokenDto kakaoTokenDto = kakaoService.getKakaoAccessToken(code);
        String token = kakaoTokenDto.getAccess_token();
        TokenDto tokenDto = kakaoService.createKakaoUser(token);
        return ResponseEntity.ok().body(new CommonResponseDto(true, tokenDto));
    }
}
