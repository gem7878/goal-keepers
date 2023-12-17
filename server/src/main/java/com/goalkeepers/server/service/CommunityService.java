/*package com.goalkeepers.server.service;

import static org.junit.jupiter.api.DynamicTest.stream;

import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.goalkeepers.server.dto.PostResponseDto;
import com.goalkeepers.server.repository.GoalLikeRepository;
import com.goalkeepers.server.repository.PostRepository;
import com.goalkeepers.server.repository.PostShareRepository;

@Service
public class CommunityService {
    
    private final PostRepository postRepository;
    private final GoalLikeRepository goalLikeRepository;
    private final PostShareRepository postShareRepository;

    여러 유저의 포스트 보기
        담기
        좋아요

    public List<PostResponseDto> getAllPostList() {

        return postRepository.findAllOrderBy
                            .stream()
                            .map(PostResponseDto::of)
                            .collect(Collectors.toList());
    }
    
}
*/
