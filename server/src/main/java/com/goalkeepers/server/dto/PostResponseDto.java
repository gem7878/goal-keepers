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
	private Long originalGoalId; // Goal : origianl goal id
    private String originalGoalTitle; // Goal : origianl goal title
    private String originalGoalDescription; // Goal : origianl goal description
    private String originalGoalImageUrl; // Goal : origianl goal image_url
    private int originalGoalshareCnt; // Goal : original goal share_cnt
    private boolean isShare; // Goal : original goal, Member
	private List<Map<String, Object>> joinMemberList; // Member, Share
    private List<PostContentResponseDto> contentList; // PostContent
    
    public static PostResponseDto of(Long postId, Goal goal, 
                            String originalGoalImageUrl, 
                            boolean isShare, 
                            List<Map<String, Object>> joinMemberList, 
                            List<PostContentResponseDto> contentList) {

        return PostResponseDto.builder()
            .postId(postId)
            .originalGoalId(goal.getId())
            .originalGoalTitle(goal.getTitle())
            .originalGoalDescription(goal.getDescription())
            .originalGoalImageUrl(originalGoalImageUrl)
            .originalGoalshareCnt(goal.getShareCnt())
            .isShare(isShare)
            .joinMemberList(joinMemberList)
            .contentList(contentList)
            .build();
    }
}
