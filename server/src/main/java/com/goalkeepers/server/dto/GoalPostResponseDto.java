package com.goalkeepers.server.dto;

import com.goalkeepers.server.entity.Post;

import java.time.LocalDateTime;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GoalPostResponseDto {
    private Long postId;
    private String nickname;
    private String title;
    private String content;
    private LocalDateTime updatedAt;
    private int likeCnt;

    public static GoalPostResponseDto of(Post post) {
        return GoalPostResponseDto.builder()
                .postId(post.getId())
                .nickname(post.getMember().getNickname())
                .title(post.getTitle())
                .content(post.getContent())
                .updatedAt(post.getUpdatedAt())
                .likeCnt(post.getLikeCnt())
                .build();
    }
}
