package com.goalkeepers.server.dto;

import java.util.List;
import java.util.Map;

import com.goalkeepers.server.entity.Goal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PostResponseDto {
    private Long postId; // Post
	private Long goalId; // Goal : origianl goal id
    private String goalTitle; // Goal : origianl goal title
    private String goalDescription; // Goal : origianl goal description
    private String goalImageUrl; // Goal : origianl goal image_url
    private int goalshareCnt; // Goal : original goal share_cnt
    private boolean isShare; // Goal : original goal, Member
    private PostContentResponseDto content; // PostContent
    
    public static PostResponseDto of(Long postId, Goal goal, 
                            String goalImageUrl, 
                            boolean isShare, 
                            PostContentResponseDto content) {

        return PostResponseDto.builder()
            .postId(postId)
            .goalId(goal.getId())
            .goalTitle(goal.getTitle())
            .goalDescription(goal.getDescription())
            .goalImageUrl(goalImageUrl)
            .goalshareCnt(goal.getShareCnt())
            .isShare(isShare)
            .content(content)
            .build();
    }
}
