package com.goalkeepers.server;

import java.util.Optional;
import java.util.Date;

import javax.crypto.SecretKey;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import com.goalkeepers.server.model.User;
import com.goalkeepers.server.repository.UserRepository;
import com.goalkeepers.server.service.UserService;
import com.goalkeepers.server.util.JwtTokenUtil;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.transaction.Transactional;

@SpringBootTest
@Transactional
class ServerApplicationTests {

	@Autowired
	private UserRepository userRepository;

	private String jwtSecret;

	@Value("${app.jwt.secret}")
	public void setJwtSecret(String jwtSecret) {
		this.jwtSecret = jwtSecret;
	}

	@Test
	public void 로그인() {
		String email = "abcd@gogo.com";
		String password = "passtest";

		User newUser = new User();
        newUser.setEmail(email);
        newUser.setNickname("닉네임");
        newUser.setPassword(password);
        userRepository.save(newUser);

		Optional<User> existingUser = userRepository.findByEmail(email);
		
		Assertions.assertEquals(email, existingUser.get().getEmail());
	}

	@Test
	public void createJwt() {
        // 토큰 만료 시간 설정
		long expirationMs = System.currentTimeMillis() + 1000*60*60*1; // 1시간

		// 시크릿 키
		SecretKey key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));

		// 토큰 생성
		String jws = Jwts.builder()
						.subject("user")
						.expiration(new Date(expirationMs))
						.issuedAt(new Date())
						.signWith(key)
						.compact();    
		
		// 토큰 확인
		assert Jwts.parser().verifyWith(key).build().parseSignedClaims(jws).getPayload().getSubject().equals("user");  
    }

}
