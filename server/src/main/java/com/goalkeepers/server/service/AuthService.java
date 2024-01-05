package com.goalkeepers.server.service;

import java.util.Optional;
import java.util.UUID;
import java.util.Random;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.goalkeepers.server.dto.LoginRequestDto;
import com.goalkeepers.server.dto.MemberRequestDto;
import com.goalkeepers.server.dto.MemberResponseDto;
import com.goalkeepers.server.dto.TokenDto;
import com.goalkeepers.server.entity.EmailCode;
import com.goalkeepers.server.entity.Member;
import com.goalkeepers.server.exception.CustomException;
import com.goalkeepers.server.jwt.TokenProvider;
import com.goalkeepers.server.repository.EmailCodeRepository;
import com.goalkeepers.server.repository.MemberRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthService extends CommonService {
    private final AuthenticationManagerBuilder managerBuilder;
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;
    private final MailService mailService;
    private final EmailCodeRepository codeRepository;

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

    // 지울 예정
    public void confirmDuplicateEmail(String email) {
        Boolean isExistsEmail = memberRepository.existsByEmail(email);
        if (isExistsEmail) {
            throw new CustomException("이미 가입된 이메일입니다.");
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


    /**
     * 메일 인증
     */
    public void sendCodeToEmail(String toEmail) {
        String title = "[골키퍼스] 이메일 인증 번호가 발급되었습니다.";
        String authCode = "코드는 [" + this.createCode() + "] 입니다. 30분이내에 인증해주세요.";
        mailService.sendEmail(toEmail, title, authCode);
        if(codeRepository.existsByEmail(toEmail)) {
            codeRepository.deleteByEmail(toEmail);
        }
        codeRepository.save(new EmailCode(toEmail, authCode));
    }

    private String createCode(){
        int leftLimit = 48;
        int rightLimit = 122;
        int targetStringLength = 10;
        Random random = new Random();
        String generatedString = random.ints(leftLimit, rightLimit + 1)
                                    .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
                                    .limit(targetStringLength)
                                    .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                                    .toString();
        return generatedString;
    }

    public Boolean verifiedCode(String email, String authCode) {
        this.confirmDuplicateEmail(email);
        EmailCode code = codeRepository.findByEmail(email)
                                        .orElseThrow(() -> new CustomException("최신 인증 메일의 코드가 아니거나 인증 메일을 받은 이메일이 아닙니다."));
        
        if (code.getCode().equals(authCode)) {
            return true;
        } else {
            throw new CustomException("잘못된 코드를 입력하였습니다. 다시 확인해주세요.");
        }
    }

    /**
     * 비밀번호 찾기
     */
    public void sendPasswordToEmail(String toEmail) {
        String title = "[골키퍼스] 임시 비밀번호가 발급되었습니다.";
        String content = "임시 비밀번호는 [" + this.findPassword(toEmail) + "] 입니다.";
        
        mailService.sendEmail(toEmail, title, content);
    }

    @Transactional
    private String findPassword(String email) {
        Member member = memberRepository.findByEmail(email)
                                        .orElseThrow(() -> new CustomException("가입되지 않은 이메일입니다."));

        String newPassword = UUID.randomUUID().toString().substring(0,12);
        member.setPassword(passwordEncoder.encode(newPassword));

        return newPassword;
    }
}
