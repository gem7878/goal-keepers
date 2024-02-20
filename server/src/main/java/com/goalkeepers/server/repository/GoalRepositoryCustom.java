package com.goalkeepers.server.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.goalkeepers.server.dto.CommunityResponseDto;
import com.goalkeepers.server.dto.GoalResponseDto;
import com.goalkeepers.server.entity.SORT;

public interface GoalRepositoryCustom {
    Page<GoalResponseDto> searchMyAllGoal(Pageable pageable, Long memberId);
    Page<CommunityResponseDto> getAllNewGoal(Pageable pageable);
    Page<CommunityResponseDto> getAllPopularGoal(Pageable pageable);
    Page<CommunityResponseDto> searchCommunity(Pageable pageable, String query, SORT sort);
}