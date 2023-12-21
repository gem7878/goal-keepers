package com.goalkeepers.server.dto;

import com.goalkeepers.server.entity.Member;
import com.goalkeepers.server.entity.Post;
import com.goalkeepers.server.entity.PostComment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CommentRequestDto {
    private String content;
    
    public PostComment toComment(Member member, Post post) {
        return PostComment.builder()
                .content(content)
                .member(member)
                .post(post)
                .build();
    }
}
