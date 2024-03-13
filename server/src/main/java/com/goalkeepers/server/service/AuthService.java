package com.goalkeepers.server.service;

import java.util.UUID;
import java.util.Random;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.goalkeepers.server.common.ServiceHelper;
import com.goalkeepers.server.dto.LoginRequestDto;
import com.goalkeepers.server.dto.MemberRequestDto;
import com.goalkeepers.server.dto.TokenDto;
import com.goalkeepers.server.entity.EmailCode;
import com.goalkeepers.server.entity.Member;
import com.goalkeepers.server.entity.SNS;
import com.goalkeepers.server.exception.CustomException;
import com.goalkeepers.server.exception.ErrorCode;
import com.goalkeepers.server.jwt.TokenProvider;
import com.goalkeepers.server.repository.EmailCodeRepository;
import com.goalkeepers.server.repository.MemberRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthService extends ServiceHelper {
    private final AuthenticationManagerBuilder managerBuilder;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;
    private final MemberRepository memberRepository;
    private final EmailCodeRepository codeRepository;
    private final MailService mailService;

    // 회원가입
    public Member signup(MemberRequestDto requestDto) {
        isExistsEmail(memberRepository, requestDto.getEmail());
        isExistsNickname(memberRepository, requestDto.getNickname());
        return memberRepository.save(requestDto.toMember(passwordEncoder));
    }

    // 로그인
    public TokenDto login(LoginRequestDto requestDto) {
        UsernamePasswordAuthenticationToken authenticationToken = requestDto.toAuthentication();
        Authentication authentication = managerBuilder.getObject().authenticate(authenticationToken);
        return tokenProvider.createJwt(authentication,24);
    }

    // 이메일 중복 확인
    public void confirmDuplicateEmail(String email) {
        isExistsEmail(memberRepository, email);
    }

    // 닉네임 중복확인
    public void confirmDuplicateNickname(String nickname) {       
        isExistsNickname(memberRepository, nickname);
    }

    /*
     * 이메일 인증
     */

    // 인증 코드 이메일 보내기
    public void sendCodeToEmail(String toEmail) {
        String title = "[골키퍼스] 이메일 인증 코드가 발급되었습니다.";
        String code = this.createCode();
        String text = "코드는 [" + code + "] 입니다. 30분이내에 인증해주세요.";
        mailService.sendEmail(toEmail, title, text);
        if(codeRepository.existsByEmail(toEmail)) {
            codeRepository.deleteAllByEmail(toEmail);
        }
        codeRepository.save(new EmailCode(toEmail, code));
    }

    // 코드 만들기
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

    // 인증 코드 확인
    public Boolean verifiedCode(String email, String authCode) {
        this.confirmDuplicateEmail(email);
        EmailCode code = codeRepository.findByEmail(email)
                                        .orElseThrow(() -> new CustomException(ErrorCode.RESOURCE_NOT_FOUND, "최신 인증 메일의 코드가 아니거나 인증 메일을 받은 이메일이 아닙니다."));
        
        if (code.getCode().equals(authCode)) {
            return true;
        } else {
            throw new CustomException(ErrorCode.RESOURCE_NOT_FOUND, "잘못된 코드를 입력하였습니다. 다시 확인해주세요.");
        }
    }

    /*
     * 비밀번호 찾기
     */

    // 임시 비밀번호 이메일 보내기
    public void sendPasswordToEmail(String toEmail) {
        String title = "[골키퍼스] 임시 비밀번호가 발급되었습니다.";
        String content = "임시 비밀번호는 [" + this.createNewPassword(toEmail) + "] 입니다.";
        
        mailService.sendEmail(toEmail, title, content);
    }

    // 임시 비밀번호 만들기
    @Transactional
    private String createNewPassword(String email) {
        Member member = memberRepository.findByEmail(email)
                                        .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND, "가입되지 않은 이메일입니다."));
        SNS sns = member.getSns();
        if (sns != null) {
            String snsKorean = "";
            if (sns == SNS.KAKAO) {
                snsKorean = "카카오";
            } else if (sns == SNS.NAVER) {
                snsKorean = "네이버";
            }
            throw new CustomException(ErrorCode.BAD_REQUEST, snsKorean+ "를 통해 로그인된 이메일입니다. " + snsKorean + " 로그인을 이용해주세요.");
        }

        String newPassword = UUID.randomUUID().toString().substring(0,12);
        member.setPassword(passwordEncoder.encode(newPassword));

        return newPassword;
    }
}
