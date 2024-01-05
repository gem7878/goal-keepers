package com.goalkeepers.server.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.goalkeepers.server.dto.PostListPageResponseDto;
import com.goalkeepers.server.entity.Member;

public interface PostRepositoryCustom {
    Page<PostListPageResponseDto> searchAll(Pageable pageable);
    Page<PostListPageResponseDto> searchMyAllPost(Pageable pageable, Member member);
}
