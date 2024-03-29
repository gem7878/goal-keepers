package com.goalkeepers.server.dto;

import com.goalkeepers.server.entity.Goal;
import com.goalkeepers.server.entity.Member;
import com.goalkeepers.server.entity.Post;
import com.goalkeepers.server.entity.PostContent;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PostRequestDto {

    @NotBlank(message = "내용을 입력해주세요.")
    @Size(max = 60, message = "60자 이하로 작성하시오.")
    private String content;

    @NotNull(message = "Goal을 선택해주세요.")
    private Long goalId;

    @NotNull(message = "공개 여부를 선택해주세요")
    private Boolean privated;

    public PostContent toPostContent(Member member, Goal goal, Post post) {
        return PostContent.builder()
                .content(content)
                .shareGoal(goal)
                .post(post)
                .member(member)
                .build();
    }
}
