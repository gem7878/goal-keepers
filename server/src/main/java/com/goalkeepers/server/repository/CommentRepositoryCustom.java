package com.goalkeepers.server.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.goalkeepers.server.dto.CommentResponseDto;

public interface CommentRepositoryCustom {
    Page<CommentResponseDto> searchAllwithPost(Pageable pageable, Long postId);
}
