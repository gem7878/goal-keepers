package com.goalkeepers.server.dto;

import java.time.LocalDateTime;

import com.goalkeepers.server.entity.Member;
import com.goalkeepers.server.entity.Post;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PostResponseDto {
    private Long postId;
    private String title;
    private String content;
    private int likeCnt;
    private int shareCnt;
    private LocalDateTime updatedAt;
    private String nickname;

    public static PostResponseDto of(Post post, Member member) {
        return PostResponseDto.builder()
                .postId(post.getId())
                .nickname(member.getNickname())
                .title(post.getTitle())
                .content(post.getContent())
                .likeCnt(post.getLikeCnt())
                .shareCnt(post.getShareCnt())
                .updatedAt(post.getUpdatedAt())
                .build();     
    }
}
