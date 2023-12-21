package com.goalkeepers.server.dto;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;

import com.goalkeepers.server.entity.Goal;
import com.goalkeepers.server.entity.Member;
import com.goalkeepers.server.entity.Post;
import com.goalkeepers.server.entity.Role;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PostRequestDto {
    private String title;
    private String content;
    private Long goalId;

    public Post toPost(Member member, Goal goal) {
        return Post.builder()
                .title(title)
                .content(content)
                .goal(goal)
                .member(member)
                .build();
    }
}