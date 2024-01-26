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
public class CommunityResponseDto {
	private Long originalGoalId; // Goal : origianl goal id
    private String originalGoalTitle; // Goal : origianl goal title
    private String originalGoalDescription; // Goal : origianl goal description
    private String originalGoalImageUrl; // Goal : origianl goal image_url
    private int originalGoalshareCnt; // Goal : original goal share_cnt
    private boolean isShare; // Goal : original goal, Member
    
    public static CommunityResponseDto of(Long postId, Goal goal, 
                            String originalGoalImageUrl, 
                            boolean isShare) {

        return CommunityResponseDto.builder()
            .originalGoalId(goal.getId())
            .originalGoalTitle(goal.getTitle())
            .originalGoalDescription(goal.getDescription())
            .originalGoalImageUrl(originalGoalImageUrl)
            .originalGoalshareCnt(goal.getShareCnt())
            .isShare(isShare)
            .build();
    }
}
