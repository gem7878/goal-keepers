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
    private Long postId;
	private Long goalId;
    private String goalTitle;
    private String goalDescription;
    private String goalImageUrl;
    private int goalshareCnt;
    private boolean isShare;
    private PostContentResponseDto content;
    
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
