package com.goalkeepers.server.service;

import java.util.List;
import java.util.Objects;

import org.springframework.context.annotation.DependsOn;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.goalkeepers.server.dto.PostContentUpdateRequestDto;
import com.goalkeepers.server.dto.PostMyResponseDto;
import com.goalkeepers.server.dto.PostRequestDto;
import com.goalkeepers.server.dto.PostResponseDto;
import com.goalkeepers.server.entity.Goal;
import com.goalkeepers.server.entity.Member;
import com.goalkeepers.server.entity.Post;
import com.goalkeepers.server.entity.PostContent;
import com.goalkeepers.server.exception.CustomException;
import com.goalkeepers.server.repository.GoalRepository;
import com.goalkeepers.server.repository.MemberRepository;
import com.goalkeepers.server.repository.PostContentRepository;
import com.goalkeepers.server.repository.PostRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
@DependsOn("firebaseStorageService")
public class BoardService extends CommonService {
    
    private final PostRepository postRepository;
    private final GoalRepository goalRepository;
    private final MemberRepository memberRepository;
    private final PostContentRepository contentRepository;
    private final LikeShareService likeShareService;
    

    /*
     * 모든 게시글 가져오기*
     * 나의 모든 게시글 가져오기*
     * 하나의 게시글 가져오기*
     * 검색하기*
     */

    public Page<PostResponseDto> getAllPostList(int pageNumber) {
        return postRepository.getAll(PageRequest.of(pageNumber - 1, 10));
    }

    public Page<PostMyResponseDto> getMyAllPostList(int pageNumber) {
        Member member = isMemberCurrent(memberRepository);
        return postRepository.getMyAllPost(PageRequest.of(pageNumber - 1, 10), member);
    }

    public PostResponseDto getOnePost(Long postId) {
        // 하나의 포스트에서 일단 최신 20개만 보여줌
        return postRepository.getOnePost(PageRequest.of(0, 20), isPost(postRepository, postId));
    }

    public Page<PostResponseDto> searchGoalAndPost(int pageNumber, String query, String sort) {
        return postRepository.searchAll(PageRequest.of(pageNumber - 1, 10), query, sort);
    }

    /*
     * 게시글 쓰기*
     * 게시글 수정하기*
     * 게시글 삭제하기*
     */

    public Long createMyPostContent(PostRequestDto requestDto) {
        Member member = isMemberCurrent(memberRepository);
        Goal goal = isMyGoal(memberRepository, goalRepository, requestDto.getGoalId());
        Goal originalGoal = Objects.nonNull(goal.getShare()) ? goal.getShare().getGoal() : goal;
        
        Post post = null;
        if(Objects.nonNull(originalGoal)) {
            post = postRepository.findByOriginalGoal(originalGoal).orElse(null);
            if(Objects.isNull(post)) {
                post = postRepository.save(new Post(originalGoal));
            }
        } else {
            throw new CustomException("원본 goal을 찾을 수 없습니다.");
        } 

        PostContent content = contentRepository.save(requestDto.toPostContent(member, goal, post));
        return content.getId();
    }

    public void updateMyPostContent(PostContentUpdateRequestDto requestDto, Long contentId) {
        PostContent.postUpdate(isMyPostContent(memberRepository, contentRepository, contentId), requestDto);
        // Goal goal = post.getGoal();
        // return PostResponseDto.of(post, getGoalImageUrl(goal));
    }

    public void deleteMyPostContent(Long contentId) {
        PostContent content = isMyPostContent(memberRepository, contentRepository, contentId);
        likeShareService.deleteLike(content);
        contentRepository.delete(content);
    }

    public List<PostContent> getMyPostContentWithGoal(Goal goal) {
        return contentRepository.findAllByShareGoal(goal);
    }
}

