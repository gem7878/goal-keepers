package com.goalkeepers.server.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.goalkeepers.server.service.KakaoService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/kakao")
public class KakaoController {

    @Autowired
	public KakaoService kakaoService;

	@GetMapping("/login")
    public String goKakaoOAuth() {
       return kakaoService.goKakaoOAuth();
    }

	@GetMapping("/login-callback")
	public String loginCallback(@RequestParam("code") String code) {
	   return kakaoService.loginCallback(code);
	}	
	
	@GetMapping("/profile")
    public String getProfile() {
       return kakaoService.getProfile();
    }	
	
	@PostMapping("/authorize")
    public String goKakaoOAuth(@RequestParam("scope") String scope) {
		return kakaoService.goKakaoOAuth(scope);
    }
}
