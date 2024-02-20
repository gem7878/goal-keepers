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
public class PostMyResponseDto {
    private Long postId;
    private Long myGoalId; // 나의 goal
	private Long originalGoalId; // 원본 goal (나의 goal과 같을 수 있음)
    private String myGoalTitle;
    private String myGoalDescription;
    private String myGoalImageUrl;
    // private String originalGoalTitle; // Goal : origianl goal title
    // private String originalGoalDescription; // Goal : origianl goal description
    // private String originalGoalImageUrl; // Goal : origianl goal image_url
    private int originalGoalshareCnt; // Goal : original goal share_cnt
    private boolean isShare; // Goal : original goal, Member
	private List<Map<String, Object>> joinMemberList; // Member, Share
    private List<PostContentResponseDto> myContentList; // PostContent
    
    public static PostMyResponseDto of(Long postId,
                            Goal myGoal,
                            Goal originalGoal,
                            String imageUrl, 
                            boolean isShare, 
                            List<Map<String, Object>> joinMemberList, 
                            List<PostContentResponseDto> contentList) {

        return PostMyResponseDto.builder()
            .postId(postId)
            .myGoalId(myGoal.getId())
            .originalGoalId(originalGoal.getId())
            .myGoalTitle(myGoal.getTitle())
            .myGoalDescription(myGoal.getDescription())
            .myGoalImageUrl(imageUrl)
            .originalGoalshareCnt(originalGoal.getShareCnt())
            .isShare(isShare)
            .joinMemberList(joinMemberList)
            .myContentList(contentList)
            .build();
    }
}
