package com.goalkeepers.server.dto;

import com.goalkeepers.server.entity.Goal;
import com.goalkeepers.server.entity.Member;
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
    private String nickname;
    private int postCheerCnt;
    private boolean isCheer;
	private Long goalId;
    private String goalTitle;
    private String goalDescription;
    private String goalImageUrl;
    private int goalshareCnt;
    private boolean isShare;
    private boolean isMyPost;
    private PostContentResponseDto content;
    
    public static PostResponseDto of(
                    Post post,
                    Member member,
                    boolean isCheer,
                    boolean isMyPost,
                    Goal goal, 
                    String goalImageUrl, 
                    boolean isShare,
                    int goalShareCnt,
                    PostContentResponseDto content) {

        return PostResponseDto.builder()
            .postId(post.getId())
            .nickname(member.getNickname())
            .postCheerCnt(post.getCheerCnt())
            .isCheer(isCheer)
            .goalId(goal.getId())
            .goalTitle(goal.getTitle())
            .goalDescription(goal.getDescription())
            .goalImageUrl(goalImageUrl)
            .goalshareCnt(goalShareCnt)
            .isShare(isShare)
            .isMyPost(isMyPost)
            .content(content)
            .build();
    }
}
