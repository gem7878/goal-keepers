package com.goalkeepers.server.service;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.goalkeepers.server.dto.MemberRequestDto;
import com.goalkeepers.server.dto.MemberResponseDto;
import com.goalkeepers.server.dto.TokenDto;
import com.goalkeepers.server.entity.Member;
import com.goalkeepers.server.jwt.TokenProvider;
import com.goalkeepers.server.repository.MemberRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthService {
    private final AuthenticationManagerBuilder managerBuilder;
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;

    public MemberResponseDto signup(MemberRequestDto requestDto) {
        if(memberRepository.existsByEmail(requestDto.getEmail())) {
            throw new RuntimeException("이미 가입된 이메일입니다.");
        }

        Member member = requestDto.toMember(passwordEncoder);
        return MemberResponseDto.of(memberRepository.save(member));
    }

    public TokenDto login(MemberRequestDto requestDto) {
        UsernamePasswordAuthenticationToken authenticationToken = requestDto.toAuthentication();
        Authentication authentication = managerBuilder.getObject().authenticate(authenticationToken);
        return tokenProvider.createJwt(authentication,3);
    }

    // 이메일 중복확인
    public Map<String, Object> confirmDuplicateEmail(String email) {
        Boolean isExistsEmail = memberRepository.existsByEmail(email);
        Map<String, Object> response = new HashMap<>();
        
        if (isExistsEmail) {
            response.put("success", false);
            response.put("message", "이미 가입된 이메일입니다.");
        } else {
            response.put("success", true);
            response.put("message", "사용 가능한 이메일입니다.");
            response.put("email", email);
        }
        //System.out.println(response);
        return response;
    }

    // 닉네임 중복확인
    public Map<String, Object> confirmDuplicateNickname(String nickname) {
        Boolean isExistsNickname = memberRepository.existsByNickname(nickname);
        Map<String, Object> response = new HashMap<>();
        
        if (isExistsNickname) {
            response.put("success", false);
            response.put("message", "사용중인 닉네임입니다.");
        } else {
            response.put("success", true);
            response.put("message", "사용 가능한 닉네임입니다.");
            response.put("nickname", nickname);
        }
        //System.out.println(response);
        return response;
    }
}
