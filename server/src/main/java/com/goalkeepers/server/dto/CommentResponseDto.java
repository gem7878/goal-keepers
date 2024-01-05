package com.goalkeepers.server.dto;

import java.time.LocalDateTime;

import com.goalkeepers.server.entity.PostComment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CommentResponseDto {
    private Long commentId;
    private String content;
    private String nickname;
    private LocalDateTime updatedAt;
    private Boolean isMyComment;
    
    public static CommentResponseDto of(PostComment comment, Boolean isMyComment) {
        return CommentResponseDto.builder()
                .commentId(comment.getId())
                .content(comment.getContent())
                .nickname(comment.getMember().getNickname())
                .updatedAt(comment.getUpdatedAt())
                .isMyComment(isMyComment)
                .build();
    }
}
