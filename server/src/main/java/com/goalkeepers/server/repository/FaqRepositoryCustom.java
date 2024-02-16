package com.goalkeepers.server.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.goalkeepers.server.dto.FAQResponseDto;

public interface FaqRepositoryCustom {
    Page<FAQResponseDto> findAllByPage(Pageable pageable);
}
