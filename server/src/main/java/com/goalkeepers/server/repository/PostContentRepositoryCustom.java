package com.goalkeepers.server.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.goalkeepers.server.dto.PostContentResponseDto;
import com.goalkeepers.server.dto.PostResponseDto;
import com.goalkeepers.server.dto.TargetResponseDto;
import com.goalkeepers.server.entity.Goal;
import com.goalkeepers.server.entity.Member;
import com.goalkeepers.server.entity.Post;
import com.goalkeepers.server.entity.SORT;
import com.goalkeepers.server.entity.TYPE;

public interface PostContentRepositoryCustom {
    Page<PostResponseDto> getAllContentAndGoal(Pageable pageable, SORT sort);
    Page<PostResponseDto> getMyAllContentAndGoal(Pageable pageable, Member member);
    Page<PostContentResponseDto> getPostContents(Pageable pageable, Post post);
    Page<PostContentResponseDto> getCommunityContents(Pageable pageable, Goal goal);
    Page<PostResponseDto> searchPost(Pageable pageable, String query, SORT sort);
    TargetResponseDto findTarget(TYPE type, Long targetId, Long commentId);
}