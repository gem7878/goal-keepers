/*package com.goalkeepers.server.controller;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.goalkeepers.server.form.UserLoginForm;
import com.goalkeepers.server.form.UserRegisterForm;
import com.goalkeepers.server.model.User;
import com.goalkeepers.server.service.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class UserController {
    private final UserService userService;

    @PostMapping("/users/email")
    public ResponseEntity<Object> confirmDuplicateEmail(@RequestBody Map<String, String> emailMap) {
        Boolean isDuplicate = userService.confirmDuplicateEmail(emailMap.get("email"));
        if (!isDuplicate) {
            return ResponseEntity.ok("이메일을 사용할 수 있습니다.");
        }
        return ResponseEntity.badRequest().body("중복된 이메일이 존재합니다.");
    }

    @PostMapping("/users")
    public ResponseEntity<?> registerUser(@Valid @RequestBody UserRegisterForm user, BindingResult bindingResult) {
        
        if (bindingResult.hasErrors()) {
            List<String> errorMessages = bindingResult.getAllErrors().stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.toList());

            return ResponseEntity.badRequest().body(errorMessages);
        }
        User newUser = userService.registerUser(user);
        return new ResponseEntity<>(newUser, HttpStatus.CREATED);
    }

    @PostMapping("/users/login")
    public ResponseEntity<?> loginUser(@Valid @RequestBody UserLoginForm user, BindingResult bindingResult) {
        // 요청 데이터에 에러가 있을 때
        if (bindingResult.hasErrors()) {
            List<String> errorMessages = bindingResult.getAllErrors().stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.toList());

            return ResponseEntity.badRequest().body(errorMessages);
        }
        
        return ResponseEntity.ok(userService.loginUser(user));
    }
}*/
