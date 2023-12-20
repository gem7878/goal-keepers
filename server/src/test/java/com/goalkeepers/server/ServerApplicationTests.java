package com.goalkeepers.server;

import java.util.Optional;
import java.util.Date;

import javax.crypto.SecretKey;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import com.goalkeepers.server.repository.MemberRepository;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.transaction.Transactional;

@SpringBootTest
@Transactional
class ServerApplicationTests {

	@Autowired
	private MemberRepository memberRepository;

	private String jwtSecret;

	@Value("${app.jwt.secret}")
	public void setJwtSecret(String jwtSecret) {
		this.jwtSecret = jwtSecret;
	}

}
