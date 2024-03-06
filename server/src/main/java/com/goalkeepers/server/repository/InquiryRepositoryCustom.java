package com.goalkeepers.server.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.goalkeepers.server.dto.InquiryResponseDto;
import com.goalkeepers.server.entity.Member;

public interface InquiryRepositoryCustom {
    Page<InquiryResponseDto> findAllByPage(Pageable pageable, Member member);
}
