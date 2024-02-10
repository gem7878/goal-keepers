package com.goalkeepers.server.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.goalkeepers.server.dto.NotificationResponseDto;
import com.goalkeepers.server.entity.Member;
import com.goalkeepers.server.entity.TYPE;

public interface NotificationRepositoryCustom {
    Page<NotificationResponseDto> findAllByMember(Pageable pageable, Member member, TYPE type);
    Long readAllAlarms(Member member);
    Long deleteAllAlarms(Member member);
    Long deleteSelectedAlarms(Member member, List<Long> idList);
}
