package com.goalkeepers.server.service;

import java.util.Objects;

import org.springframework.context.annotation.DependsOn;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.goalkeepers.server.common.ServiceHelper;
import com.goalkeepers.server.dto.CommunityContentResponseDto;
import com.goalkeepers.server.dto.PostContentResponseDto;
import com.goalkeepers.server.dto.PostRequestDto;
import com.goalkeepers.server.dto.PostResponseDto;
import com.goalkeepers.server.entity.Goal;
import com.goalkeepers.server.entity.Member;
import com.goalkeepers.server.entity.Post;
import com.goalkeepers.server.entity.PostContent;
import com.goalkeepers.server.entity.SORT;
import com.goalkeepers.server.repository.GoalRepository;
import com.goalkeepers.server.repository.MemberRepository;
import com.goalkeepers.server.repository.PostContentRepository;
import com.goalkeepers.server.repository.PostRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
@DependsOn("firebaseStorageService")
public class ContentService extends ServiceHelper {

    private final MemberRepository memberRepository;
    private final GoalRepository goalRepository;
    private final PostContentRepository contentRepository;
    private final PostRepository postRepository;
    private final LikeShareService likeShareService;


    /*
     * 게시글 쓰기
     * 게시글 삭제하기*
     */

    public Long createMyPostContent(PostRequestDto requestDto) {
        Member member = isMemberCurrent(memberRepository);

        Goal goal = isMyGoal(memberRepository, goalRepository, requestDto.getGoalId());

        Post post = postRepository.findByGoal(goal).orElse(null);

        if(Objects.isNull(post)) {
            post = postRepository.save(new Post(goal, requestDto.getPrivated()));
        } else if (post.isPrivated() != requestDto.getPrivated()) {
            post.setPrivated(requestDto.getPrivated());
        }

        // 담기한 Goal이면 shareGoal로 바꾸기
        if(Objects.nonNull(goal.getShare()) && Objects.nonNull(goal.getShare().getGoal())) {
            goal = goal.getShare().getGoal();
        }

        // 컨텐트 생성
        PostContent content = contentRepository.save(requestDto.toPostContent(member, goal, post));
        return content.getId();
    }

    public void deleteMyPostContent(Long contentId) {
        PostContent content = isMyPostContent(memberRepository, contentRepository, contentId);
        likeShareService.deleteLike(content);
        contentRepository.delete(content);
    }

    /*
     * 모든 포스트 가져오기
     * 나의 모든 포스트 가져오기
     */

    public Page<PostResponseDto> getAllPost(int pageNumber, SORT sort) {
        return contentRepository.getAllContentAndGoal(PageRequest.of(pageNumber - 1, 20), sort);
    }

    public Page<PostResponseDto> getMyAllPost(int pageNumber) {
        return contentRepository.getMyAllContentAndGoal(PageRequest.of(pageNumber - 1, 20), isMemberCurrent(memberRepository));
    }

    /*
     * 포스트 컨텐트 가져오기
     * 커뮤니티 컨텐츠 가져오기
     */

    public Page<PostContentResponseDto> getPostContents(Long postId, int pageNumber) {
        return contentRepository.getPostContents(PageRequest.of(pageNumber - 1, 10), isPost(postRepository, postId));
    }

    public Page<CommunityContentResponseDto> getCommunityContents(Long goalId, int pageNumber) {
        return contentRepository.getCommunityContents(PageRequest.of(pageNumber - 1, 10), isGoal(goalRepository, goalId));
    }
}
