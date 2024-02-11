package com.goalkeepers.server.dto;

import java.time.LocalDateTime;
import java.util.Objects;

import com.goalkeepers.server.entity.Goal;
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
    private Long contentId;
    private String nickname; // Member
    private String content; // PostContent
    private LocalDateTime createdAt; // PostContent
    private int likeCnt; // PostContent
    private boolean isLike; // PostContent, Member
    private Long goalId; // Goal : share goal id
    private String goalTitle; // Goal : share goal title
    private String goalDescription; // Goal : share goal description
    private String goalImageUrl; // Goal : share goal image_url

    public static PostContentResponseDto of(PostContent postContent, Goal goal, String nickname, boolean isLike, String imageUrl) {
        boolean isGoal = Objects.nonNull(goal);
        return PostContentResponseDto.builder()
            .contentId(postContent.getId())
            .nickname(nickname)
            .content(postContent.getContent())
            .createdAt(postContent.getCreatedAt())
            .likeCnt(postContent.getLikeCnt())
            .isLike(isLike)
            .goalId(isGoal ? goal.getId() : null)
            .goalTitle(isGoal ? goal.getTitle() : null)
            .goalDescription(isGoal ? goal.getDescription() : null)
            .goalImageUrl(imageUrl)
            .build();
    }
}
