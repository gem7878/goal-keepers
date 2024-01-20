package com.goalkeepers.server.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.goalkeepers.server.dto.PostMyResponseDto;
import com.goalkeepers.server.dto.PostResponseDto;
import com.goalkeepers.server.entity.Member;
import com.goalkeepers.server.entity.Post;

public interface PostRepositoryCustom {
    Page<PostResponseDto> getAll(Pageable pageable);
    Page<PostMyResponseDto> getMyAllPost(Pageable pageable, Member member);
    Page<PostResponseDto> searchAll(Pageable pageable, String query, String sort);
    PostResponseDto getOnePost(Pageable pageable, Post post);
}
