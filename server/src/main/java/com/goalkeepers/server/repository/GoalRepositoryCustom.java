package com.goalkeepers.server.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.goalkeepers.server.dto.GoalResponseDto;

public interface GoalRepositoryCustom {
    Page<GoalResponseDto> searchMyAllGoal(Pageable pageable, Long memberId);
}