package com.goalkeepers.server.service;

import org.springframework.context.annotation.DependsOn;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.goalkeepers.server.common.ServiceHelper;
import com.goalkeepers.server.config.SecurityUtil;
import com.goalkeepers.server.dto.CommunityResponseDto;
import com.goalkeepers.server.dto.PostSelectResponseDto;
import com.goalkeepers.server.dto.PostResponseDto;
import com.goalkeepers.server.entity.Goal;
import com.goalkeepers.server.entity.Member;
import com.goalkeepers.server.entity.Post;
import com.goalkeepers.server.entity.SORT;
import com.goalkeepers.server.exception.CustomException;
import com.goalkeepers.server.exception.ErrorCode;
import com.goalkeepers.server.repository.GoalRepository;
import com.goalkeepers.server.repository.MemberRepository;
import com.goalkeepers.server.repository.PostContentRepository;
import com.goalkeepers.server.repository.PostRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
@DependsOn("firebaseStorageService")
public class BoardService extends ServiceHelper {
    
    private final PostContentRepository contentRepository;
    private final GoalRepository goalRepository;
    private final PostRepository postRepository;
    private final MemberRepository memberRepository;

    /*
     * 커뮤니티 최신순 가져오기
     * 커뮤니티 인기순 가져오기
     */

    public Page<CommunityResponseDto> getAllNewGoal(int pageNumber) {
        return goalRepository.getAllNewGoal(PageRequest.of(pageNumber - 1, 10));
    }

    public Page<CommunityResponseDto> getAllPopularGoal(int pageNumber) {
        return goalRepository.getAllPopularGoal(PageRequest.of(pageNumber - 1, 10));
    }
    
    /*
     * 포스트 검색
     * 커뮤니티 검색
     */

    public Page<PostResponseDto> searchPost(int pageNumber, String query, SORT sort) {
        return contentRepository.searchPost(PageRequest.of(pageNumber - 1, 10), query, sort);
    }

    public Page<CommunityResponseDto> searchCommunity(int pageNumber, String query, SORT sort) {
        return goalRepository.searchCommunity(PageRequest.of(pageNumber - 1, 10), query, sort);
    }

    /*
     * 선택할 포스트 가져오기
     */
    public Page<PostSelectResponseDto> getSelectAllPost(int pageNumber) {
        return postRepository.getSelectedAllPost(PageRequest.of(pageNumber - 1, 15), SecurityUtil.getCurrentMemberId());
    }

    /*
     * 목표 생성시 포스트 생성하기
     */
    public void createMyPost(Long goalId) {
        Goal goal = isMyGoal(memberRepository, goalRepository, goalId);
        postRepository.save(new Post(goal));
    }

    /*
     * 비공개 업데이트
     */
    public boolean updatePostPrivated(Long postId) {
        Member member = isMemberCurrent(memberRepository);
        Post post = isPost(postRepository, postId);
        if(post.getGoal().getMember().equals(member)) {
            boolean currentPrivated = post.isPrivated();
            post.setPrivated(!currentPrivated);
            return !currentPrivated;
        } else {
            throw new CustomException(ErrorCode.BAD_REQUEST);
        }
    }
}

