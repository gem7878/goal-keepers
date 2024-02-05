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
public class PostResponseDto {
    private Long postId;
    private int postCheerCnt;
    private boolean isCheer;
	private Long goalId;
    private String goalTitle;
    private String goalDescription;
    private String goalImageUrl;
    private int goalshareCnt;
    private boolean isShare;
    private PostContentResponseDto content;
    
    public static PostResponseDto of(
                    Post post, 
                    boolean isCheer, 
                    Goal goal, 
                    String goalImageUrl, 
                    boolean isShare, 
                    PostContentResponseDto content) {

        return PostResponseDto.builder()
            .postId(post.getId())
            .postCheerCnt(post.getCheerCnt())
            .isCheer(isCheer)
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
