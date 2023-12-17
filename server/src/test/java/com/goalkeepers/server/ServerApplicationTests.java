package com.goalkeepers.server;

import java.util.Optional;
import java.util.stream.Collectors;
import java.util.Date;
import java.util.List;

import javax.crypto.SecretKey;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.goalkeepers.server.config.SecurityUtil;
import com.goalkeepers.server.dto.GoalResponseDto;
import com.goalkeepers.server.dto.MemberResponseDto;
import com.goalkeepers.server.entity.Goal;
import com.goalkeepers.server.entity.Member;
import com.goalkeepers.server.entity.Role;
import com.goalkeepers.server.repository.GoalRepository;
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
	
	@Autowired
	private GoalRepository goalRepository;

	private PasswordEncoder passwordEncoder;

	@Test
	public void getMyGoalList() {
		Member testone = Member.builder()
                .email("abc@gogo.com")
                .password(passwordEncoder.encode("password"))
                .nickname("abcde")
                .role(Role.ROLE_USER)
                .build();
        memberRepository.save(testone);

        Member member = memberRepository.findByEmail("abc@gogo.com")
                                        .orElseThrow(() -> new RuntimeException("로그인 유저 정보가 없습니다."));
        
		System.out.println(goalRepository.findAllByMember(member)
                                .stream()
                                .map(GoalResponseDto::of)
                                .collect(Collectors.toList()));
    }

}
