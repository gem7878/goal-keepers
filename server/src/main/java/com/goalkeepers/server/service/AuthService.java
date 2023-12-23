package com.goalkeepers.server.service;

import java.util.Optional;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.goalkeepers.server.dto.LoginRequestDto;
import com.goalkeepers.server.dto.MemberRequestDto;
import com.goalkeepers.server.dto.MemberResponseDto;
import com.goalkeepers.server.dto.TokenDto;
import com.goalkeepers.server.entity.Member;
import com.goalkeepers.server.exception.CustomException;
import com.goalkeepers.server.jwt.TokenProvider;
import com.goalkeepers.server.repository.MemberRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

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
            throw new CustomException("이미 가입된 이메일입니다.");
        } if (memberRepository.existsByNickname(requestDto.getNickname())) {
            throw new CustomException("사용중인 닉네임입니다.");
        }

        Member member = requestDto.toMember(passwordEncoder);
        return MemberResponseDto.of(memberRepository.save(member));
    }

    public TokenDto login(LoginRequestDto requestDto) {
        UsernamePasswordAuthenticationToken authenticationToken = requestDto.toAuthentication();
        Authentication authentication = managerBuilder.getObject().authenticate(authenticationToken);
        return tokenProvider.createJwt(authentication,12);
    }

    // 멤버 정보 가져오기
    public Optional<Member> loadMemberByEmail(String email) {
        return memberRepository.findByEmail(email);
    }

    // 이메일 중복확인
    public Boolean confirmDuplicateEmail(String email) {
        Boolean isExistsEmail = memberRepository.existsByEmail(email);
        if (isExistsEmail) {
            throw new CustomException("이미 가입된 이메일입니다.");
        } else {
            return true;
        }
    }

    // 닉네임 중복확인
    public Boolean confirmDuplicateNickname(String nickname) {       
        if (memberRepository.existsByNickname(nickname)) {
            throw new CustomException("사용중인 닉네임입니다.");
        } else {
            return true;
        }
    }
}
