package com.goalkeepers.server.service;

import java.util.Optional;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.goalkeepers.server.form.UserLoginForm;
import com.goalkeepers.server.form.UserRegisterForm;
import com.goalkeepers.server.model.User;
import com.goalkeepers.server.repository.UserRepository;
import com.goalkeepers.server.util.JwtTokenUtil;

import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${app.jwt.secret}")
    private String jwtSecret;

    public Boolean confirmDuplicateEmail(String email) {
        // 중복 사용자명 확인
        Optional<User> existingUser = userRepository.findByEmail(email);
        if (existingUser.isPresent()) {
            return true;
        }
        return false;
    }

    public User registerUser(UserRegisterForm user) {
        User newUser = new User();
        newUser.setEmail(user.getEmail());
        newUser.setNickname(user.getNickName());
        newUser.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(newUser);
    }

    public Map<String, Object> loginUser(UserLoginForm user) {
        Map<String, Object> result = new HashMap<>();
    
        Optional<User> existingUser = userRepository.findByEmail(user.getEmail());
        if (existingUser.isPresent()) {
            String encodedPassword = existingUser.get().getPassword();
            if (passwordEncoder.matches(user.getPassword(), encodedPassword)) {
                String accessToken = JwtTokenUtil.createJwt(this.jwtSecret, existingUser.get().getEmail(), 3);
                String refreshToken = JwtTokenUtil.createJwt(this.jwtSecret, existingUser.get().getEmail(), 12);
                result.put("success", true);
                result.put("message", "로그인에 성공하였습니다.");
                result.put("access_token", accessToken);
                result.put("refresh_token", refreshToken);
            } else {
                result.put("success", false);
                result.put("message", "비밀번호가 다릅니다.");
            }
        } else {
            result.put("success", false);
            result.put("message", "이메일이 존재하지 않습니다.");
        }
    
        return result;
    }
    
}
