package com.goalkeepers.server.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.goalkeepers.server.dto.PostListPageResponseDto;
import com.goalkeepers.server.entity.Member;

public interface PostRepositoryCustom {
    Page<PostListPageResponseDto> getAll(Pageable pageable);
    Page<PostListPageResponseDto> getMyAllPost(Pageable pageable, Member member);
    Page<PostListPageResponseDto> searchAll(Pageable pageable, String query, String sort);
}
