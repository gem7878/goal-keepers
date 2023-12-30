package com.goalkeepers.server.dto;

import java.time.LocalDateTime;

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
    private String nickname;
    private String title;
    private String content;
    private LocalDateTime updatedAt;
    private int likeCnt;
    private Long goalId;
    private String goalTitle;
    private String goalDescription;
    private String goalImageUrl;
    private int shareCnt;

    public static PostResponseDto of(Post post) {
        if (post.getGoal() == null) {
            return PostResponseDto.builder()
                .postId(post.getId())
                .nickname(post.getMember().getNickname())
                .title(post.getTitle())
                .content(post.getContent())
                .likeCnt(post.getLikeCnt())
                .updatedAt(post.getUpdatedAt())
                .shareCnt(0)
                .goalId(null)
                .goalTitle(null)
                .goalDescription(null)
                .goalImageUrl(null)
                .build();
        }
        return PostResponseDto.builder()
                .postId(post.getId())
                .nickname(post.getMember().getNickname())
                .title(post.getTitle())
                .content(post.getContent())
                .likeCnt(post.getLikeCnt())
                .updatedAt(post.getUpdatedAt())
                .shareCnt(post.getGoal().getShareCnt())
                .goalId(post.getGoal().getId())
                .goalTitle(post.getGoal().getTitle())
                .goalDescription(post.getGoal().getDescription())
                .goalImageUrl(post.getGoal().getImageUrl())
                .build();
    }

    public static PostResponseDto listOf(Post post) {
        return PostResponseDto.builder()
                .postId(post.getId())
                .title(post.getTitle())
                .likeCnt(post.getLikeCnt())
                .updatedAt(post.getUpdatedAt())
                .build();
    }
}
