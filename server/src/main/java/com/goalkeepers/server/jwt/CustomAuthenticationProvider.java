/*package com.goalkeepers.server.jwt;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import java.util.Optional;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.goalkeepers.server.entity.Member;
import com.goalkeepers.server.service.AuthService;

import lombok.AllArgsConstructor;


@Component
@AllArgsConstructor
public class CustomAuthenticationProvider implements AuthenticationProvider {

    private final AuthService authservice;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String email = authentication.getName();
        String password = authentication.getCredentials().toString();

        Optional<Member> member = authservice.loadMemberByEmail(email);
        if (!email.equals(member.get().getEmail())) {
            throw new BadCredentialsException("가입된 이메일이 아닙니다. = " + email);
        } else if (!passwordEncoder.matches(password, member.get().getPassword())) {
            throw new BadCredentialsException("비밀번호가 다릅니다.");
        }
        return new UsernamePasswordAuthenticationToken(email, password);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return CustomAuthenticationToken.class.isAssignableFrom(authentication);
    }
}*/
