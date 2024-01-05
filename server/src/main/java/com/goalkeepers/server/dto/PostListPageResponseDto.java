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
public class PostListPageResponseDto {
    private Long postId;
    private String nickname;
    private String title;
    private String content;
    private LocalDateTime updatedAt;
    private int likeCnt;
    private boolean isLike;
    private Long goalId;
    private String goalTitle;
    private String goalDescription;
    private String goalImageUrl;
    private int shareCnt;
    private boolean isShare;

    public static PostListPageResponseDto of(Post post, boolean isLike, boolean isShare) {
        if (post.getGoal() == null) {
            return PostListPageResponseDto.builder()
                .postId(post.getId())
                .nickname(post.getMember().getNickname())
                .title(post.getTitle())
                .content(post.getContent())
                .likeCnt(post.getLikeCnt())
                .isLike(isLike)
                .updatedAt(post.getUpdatedAt())
                .goalId(null)
                .goalTitle(null)
                .goalDescription(null)
                .goalImageUrl(null)
                .shareCnt(0)
                .isShare(false)
                .build();
        } else {
            return PostListPageResponseDto.builder()
                .postId(post.getId())
                .nickname(post.getMember().getNickname())
                .title(post.getTitle())
                .content(post.getContent())
                .likeCnt(post.getLikeCnt())
                .isLike(isLike)
                .updatedAt(post.getUpdatedAt())
                .goalId(post.getGoal().getId())
                .goalTitle(post.getGoal().getTitle())
                .goalDescription(post.getGoal().getDescription())
                .goalImageUrl(post.getGoal().getImageUrl())
                .shareCnt(post.getGoal().getShareCnt())
                .isShare(isShare)
                .build();
        }
    }
}
