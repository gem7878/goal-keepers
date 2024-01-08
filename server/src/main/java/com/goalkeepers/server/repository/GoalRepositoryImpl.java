package com.goalkeepers.server.repository;

import java.util.Objects;
import java.util.List;
import java.util.stream.Collectors;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.goalkeepers.server.dto.GoalResponseDto;
import com.goalkeepers.server.entity.Goal;
import com.goalkeepers.server.service.FirebaseStorageService;
import com.querydsl.jpa.impl.JPAQueryFactory;

import static com.goalkeepers.server.entity.QGoal.goal;
import static com.goalkeepers.server.entity.QMember.member;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class GoalRepositoryImpl implements GoalRepositoryCustom {
    
    private final JPAQueryFactory queryFactory;
    private final FirebaseStorageService firebaseStorageService;

    @Override
    public Page<GoalResponseDto> searchMyAllGoal(Pageable pageable, Long memberId) {
        
        List<Goal> goals = queryFactory
                            .selectFrom(goal)
                            .leftJoin(goal.member, member)
                            .where(member.id.eq(memberId))
                            .orderBy(goal.id.desc())
                            .offset(pageable.getOffset())
                            .limit(pageable.getPageSize())
                            .fetch();

        List<GoalResponseDto> page = goals
                            .stream()
                            .map(goal -> {
                                String imageUrl = goal.getImageUrl();
                                if(Objects.nonNull(imageUrl) && !imageUrl.isEmpty()) {
                                    imageUrl = firebaseStorageService.showFile(imageUrl);
                                }
                                return GoalResponseDto.of(goal, imageUrl);
                            })
                            .collect(Collectors.toList());

        int totalSize = queryFactory
                        .selectFrom(goal)
                        .leftJoin(goal.member, member)
                        .where(member.id.eq(memberId))
                        .fetch()
                        .size();
        
        return new PageImpl<>(page, pageable, totalSize);
    }
}
