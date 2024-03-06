package com.goalkeepers.server.dto;

import java.time.LocalDateTime;
import com.goalkeepers.server.entity.PostContent;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PostContentResponseDto {
    private long contentId;
    private String nickname; // Member
    private String content; // PostContent
    private LocalDateTime createdAt; // PostContent
    private int likeCnt; // PostContent
    private boolean isLike; // PostContent, Member

    public static PostContentResponseDto of(PostContent postContent, String nickname, boolean isLike) {
        return PostContentResponseDto.builder()
            .contentId(postContent.getId())
            .nickname(nickname)
            .content(postContent.getContent())
            .createdAt(postContent.getCreatedAt())
            .likeCnt(postContent.getLikeCnt())
            .isLike(isLike)
            .build();
    }
}
