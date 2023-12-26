package com.goalkeepers.server.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.goalkeepers.server.dto.PostListPageResponseDto;

public interface PostRepositoryCustom {
    Page<PostListPageResponseDto> searchAll(Pageable pageable);
}
