package com.goalkeepers.server.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
//import io.jsonwebtoken.io.Encoders;
import io.jsonwebtoken.security.Keys;

import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

import javax.crypto.SecretKey;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.goalkeepers.server.dto.TokenDto;

import org.springframework.beans.factory.annotation.Value;

@Component
public class TokenProvider {

    private static final Logger log = LoggerFactory.getLogger(TokenProvider.class);
    private static final String AUTHORITIES_KEY = "auth";
    private static final String BEARER_TYPE = "bearer";
    private final SecretKey key;

    public TokenProvider(@Value("${jwt.secret}") String jwtSecret) {
        byte[] keyBytes = Decoders.BASE64.decode(jwtSecret);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    // 토큰 생성
    public TokenDto createJwt(Authentication authentication, long expirationHs) {
        try {
            
            String authorities = authentication.getAuthorities().stream()
                                .map(GrantedAuthority::getAuthority)
                                .collect(Collectors.joining(","));

            // 토큰 만료 시간 설정
            Date tokenExpiresIn = new Date(System.currentTimeMillis() + 1000*60*60*expirationHs);

            // 토큰 생성
            String jws = Jwts.builder()
                            .subject(authentication.getName())
                            .claim(AUTHORITIES_KEY, authorities)
                            .expiration(tokenExpiresIn)
                            .issuedAt(new Date())
                            .signWith(key)
                            .compact();
            
            // 토큰 확인
            Jwts.parser().verifyWith(key).build().parseSignedClaims(jws).getPayload().getSubject().equals(authentication.getName());                                                                                         
            
            return TokenDto.builder()
                    .grantType(BEARER_TYPE)
                    .accessToken(jws)
                    .tokenExpiresIn(tokenExpiresIn.getTime())
                    .build();

        } catch (JwtException  e) {
            log.info("토큰 생성 중 오류가 있습니다.");
        }

        return null;
    }

    // 토큰 검증
    public boolean validateToken(String token) {
        try {
            Jwts.parser().verifyWith(key).build().parseSignedClaims(token);
            return true;
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            log.info("잘못된 JWT 서명입니다.");
        } catch (ExpiredJwtException e) {
            log.info("만료된 JWT 토큰입니다.");
        } catch (UnsupportedJwtException e) {
            log.info("지원되지 않는 JWT 토큰입니다.");
        } catch (IllegalArgumentException e) {
            log.info("JWT 토큰이 잘못되었습니다.");
        }
        return false;
    }

    public Authentication getAuthentication(String token) {
        Claims claims = parseClaims(token);

        if(claims.get(AUTHORITIES_KEY) == null) {
            throw new RuntimeException("권한 정보가 없는 토큰입니다.");
        }

        Collection<? extends GrantedAuthority> authorities =
            Arrays.stream(claims.get(AUTHORITIES_KEY).toString().split(","))
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());

        UserDetails principal = new User(claims.getSubject(), "", authorities);

        return new UsernamePasswordAuthenticationToken(principal, "", authorities);
    }

    // 토큰 정보
    public Claims parseClaims(String token) {
        try {
            return Jwts.parser().verifyWith(key).build().parseSignedClaims(token).getPayload();
        } catch (ExpiredJwtException e) {
            log.info("만료된 JWT 토큰입니다.");
            return e.getClaims();
        }
    }


    /*private static void SecretKey() {
        SecretKey key = Jwts.SIG.HS256.key().build();
        String secretString = Encoders.BASE64.encode(key.getEncoded());
        System.out.println(secretString);
    }*/
}
