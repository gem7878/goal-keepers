package com.goalkeepers.server.service;

import java.util.Objects;
import java.util.List;

import org.springframework.context.annotation.DependsOn;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.goalkeepers.server.dto.PostContentResponseDto;
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
public class ContentService extends CommonService {

    private final MemberRepository memberRepository;
    private final GoalRepository goalRepository;
    private final PostContentRepository contentRepository;
    private final PostRepository postRepository;
    private final LikeShareService likeShareService;


    /*
     * 게시글 쓰기 (처음 생성할 때 postId 생성)*
     * 게시글 삭제하기*
     */

    public Long createMyPostContent(PostRequestDto requestDto) {
        Member member = isMemberCurrent(memberRepository);
        Goal goal = isMyGoal(memberRepository, goalRepository, requestDto.getGoalId());
        
        Post post = postRepository.findByGoal(goal).orElse(null);
        // 처음 컨텐트 작성할 때 postId 생성
        if(Objects.isNull(post)) {
            post = postRepository.save(new Post(goal));
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

    public Page<PostResponseDto> getAllPost(int pageNumber, String sort) {
        return contentRepository.getAllContentAndGoal(PageRequest.of(pageNumber - 1, 20));
    }

    public Page<PostMyResponseDto> getMyAllPost(int pageNumber, String sort) {
        return contentRepository.getMyAllContentAndGoal(PageRequest.of(pageNumber - 1, 20));
    }

    /*
     * 포스트의 모든 컨텐트 가져오기
     */

    public Page<PostContentResponseDto> getPostContents(Long postId, int pageNumber) {
        return contentRepository.getPostContents(PageRequest.of(pageNumber, 10), isPost(postRepository, postId));
    }

    public List<PostContent> getMyPostContentWithGoal(Goal goal) {
        return contentRepository.findAllByShareGoal(goal);
    }
}
