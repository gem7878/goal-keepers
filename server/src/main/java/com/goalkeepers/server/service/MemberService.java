package com.goalkeepers.server.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.goalkeepers.server.config.SecurityUtil;
import com.goalkeepers.server.dto.MemberResponseDto;
import com.goalkeepers.server.entity.Member;
import com.goalkeepers.server.exception.CustomException;
import com.goalkeepers.server.repository.MemberRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MemberService {
    
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional(readOnly = true)
    public MemberResponseDto getMyInfoBySecurity() {
        return memberRepository.findById(SecurityUtil.getCurrentMemberId())
                .map(MemberResponseDto::of)
                .orElseThrow(() -> new CustomException("로그인 유저 정보가 없습니다."));
    }

    // 닉네임 변경
    @Transactional
    public MemberResponseDto changeMemberNickname(String nickname) {
        Member member = isMemberCurrent();
        member.setNickname(nickname);
        return MemberResponseDto.of(memberRepository.save(member));
    }

    // 비밀번호 변경
    @Transactional
    public MemberResponseDto changeMemberPassword(String email, String exPassword, String newPassword) {
        Member member = isMemberCurrent();

        if(!member.getEmail().equals(email)) {
            throw new CustomException("로그인한 유저와 입력된 email 값이 다릅니다.");
        }

        if (!passwordEncoder.matches(exPassword, member.getPassword())) {
            throw new CustomException("원래의 비밀번호를 확인해주세요.");
        }

        if(exPassword.equals(newPassword)) {
            throw new CustomException("이전의 비밀번호와 변경하려는 비밀번호가 같습니다.");
        }

        member.setPassword(passwordEncoder.encode(newPassword));
        return MemberResponseDto.of(memberRepository.save(member));
    }

    // 로그인 했는지 확인
    public Member isMemberCurrent() {
        return memberRepository.findById(SecurityUtil.getCurrentMemberId())
                .orElseThrow(() -> new CustomException("로그인 유저 정보가 없습니다"));
    }
}
