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
	private long originalGoalId;
    private String originalGoalTitle;
    private String originalGoalDescription;
    private String originalGoalImageUrl;
    private int originalGoalshareCnt;
    private boolean isShare;
    private List<Map<String, Object>> joinMemberList;
    private List<CommunityContentResponseDto> contentList;
    private Long count; // null or long

    
    public static CommunityResponseDto of(Goal goal, 
                            String originalGoalImageUrl, 
                            boolean isShare,
                            List<Map<String, Object>> joinMemberList,
                            List<CommunityContentResponseDto> contentList,
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
