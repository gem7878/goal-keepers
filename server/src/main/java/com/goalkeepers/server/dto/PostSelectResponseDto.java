package com.goalkeepers.server.dto;

import com.goalkeepers.server.entity.Goal;
import com.goalkeepers.server.entity.Post;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PostSelectResponseDto {
    private long goalId;
    private String title;
    private String description;
    private boolean privated;
    public static PostSelectResponseDto of(Goal goal, Post post, String imageUrl) {
        return PostSelectResponseDto.builder()
                .goalId(goal.getId())
                .title(goal.getTitle())
                .description(goal.getDescription())
                .privated(post.isPrivated())
                .build();
    }

}
