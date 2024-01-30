package com.goalkeepers.server.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.goalkeepers.server.dto.CommunityResponseDto;
import com.goalkeepers.server.dto.PostContentResponseDto;
import com.goalkeepers.server.dto.PostResponseDto;
import com.goalkeepers.server.entity.Member;
import com.goalkeepers.server.entity.Post;
import com.goalkeepers.server.entity.SORT;

public interface PostContentRepositoryCustom {
    Page<PostResponseDto> getAllContentAndGoal(Pageable pageable, SORT sort);
    Page<PostResponseDto> getMyAllContentAndGoal(Pageable pageable, Member member, SORT sort);
    Page<PostContentResponseDto> getPostContents(Pageable pageable, Post post);
    Page<PostResponseDto> searchPost(Pageable pageable, String query, SORT sort);
    Page<CommunityResponseDto> searchCommunity(Pageable pageable, String query, SORT sort);
}