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
    private List<Map<String, Object>> joinMemberList;
    private List<PostContentResponseDto> contentList;
    private Long count;

    
    public static CommunityResponseDto of(Goal goal, 
                            String originalGoalImageUrl, 
                            boolean isShare,
                            List<Map<String, Object>> joinMemberList,
                            List<PostContentResponseDto> contentList,
                            Long count) {

        return CommunityResponseDto.builder()
            .originalGoalId(goal.getId())
            .originalGoalTitle(goal.getTitle())
            .originalGoalDescription(goal.getDescription())
            .originalGoalImageUrl(originalGoalImageUrl)
            .originalGoalshareCnt(goal.getShareCnt())
            .isShare(isShare)
            .joinMemberList(joinMemberList)
            .contentList(contentList)
            .count(count)
            .build();
    }
}
