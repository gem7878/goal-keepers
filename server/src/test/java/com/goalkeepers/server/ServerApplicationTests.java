package com.goalkeepers.server;

import java.util.stream.Collectors;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.goalkeepers.server.dto.GoalResponseDto;
import com.goalkeepers.server.entity.Member;
import com.goalkeepers.server.entity.Role;
import com.goalkeepers.server.repository.GoalRepository;
import com.goalkeepers.server.repository.MemberRepository;

import jakarta.transaction.Transactional;

@SpringBootTest
@Transactional
class ServerApplicationTests {

}
