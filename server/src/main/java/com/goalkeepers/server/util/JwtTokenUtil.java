package com.goalkeepers.server.util;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.io.Encoders;
import io.jsonwebtoken.security.Keys;

import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;

public class JwtTokenUtil {

    // 토큰 생성
    public static String createJwt(String jwtSecret, String user, long expirationhs) {
        try {
            // 토큰 만료 시간 설정
            long expirationMs = System.currentTimeMillis() + 1000*60*60*expirationhs; // 1시간
            
            // 시크릿 키
            SecretKey key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));

            // 토큰 생성
            String jws = Jwts.builder()
                            .subject(user)
                            .expiration(new Date(expirationMs))
                            .issuedAt(new Date())
                            .signWith(key)
                            .compact();    
            
            // 토큰 확인
            Jwts.parser().verifyWith(key).build().parseSignedClaims(jws).getPayload().getSubject().equals(user);                                                                                         
            
            return jws;

        } catch (JwtException  e) {
            System.out.println("JWT ERROR: " + e);
        }

        return null;
    }

    // 토큰 만료 시간
    public static boolean isExpired(String jwtSecret, String token) {

        // 시크릿 키
        SecretKey key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));

        Date expiredDate = Jwts.parser().verifyWith(key).build().parseSignedClaims(token).getPayload().getExpiration();
        return expiredDate.before(new Date());
    }

    public static String getUserEmail(String jwtSecret, String token) {
        // 시크릿 키
        SecretKey key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));

        return Jwts.parser().verifyWith(key).build().parseSignedClaims(token).getPayload().getSubject();
    }


    /*private static void SecretKey() {
        SecretKey key = Jwts.SIG.HS256.key().build();
        String secretString = Encoders.BASE64.encode(key.getEncoded());
        System.out.println(secretString);
    }*/
}
