package com.goalkeepers.server.service;

import java.util.List;
import java.util.Objects;

import org.springframework.context.annotation.DependsOn;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.goalkeepers.server.dto.CommunityResponseDto;
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
    
    private final PostContentRepository contentRepository;
    private final GoalRepository goalRepository;

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
     * Post Menu 검색
     * Community Menu 검색
     */

    public Page<PostResponseDto> searchPost(int pageNumber, String query, String sort) {
        return contentRepository.searchPost(PageRequest.of(pageNumber - 1, 10), query, sort);
    }

    public Page<CommunityResponseDto> searchCommunity(int pageNumber, String query, String sort) {
        if(sort.equals("popular")) {
            return contentRepository.searchCommunity(PageRequest.of(pageNumber - 1, 10), query, sort);
        }
        return goalRepository.searchCommunity(PageRequest.of(pageNumber - 1, 10), query, sort);
    }
}

