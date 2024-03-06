package com.goalkeepers.server.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.goalkeepers.server.dto.PostSelectResponseDto;

public interface PostRepositoryCustom {
    Page<PostSelectResponseDto> getSelectedAllPost(Pageable pageable, Long currentMemberId);
}
