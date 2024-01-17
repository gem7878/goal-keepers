package com.goalkeepers.server.repository;

import java.lang.Object;
import java.util.Objects;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.Optional;
import java.util.stream.Collectors;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.goalkeepers.server.dto.GoalResponseDto;
import com.goalkeepers.server.entity.Goal;
import com.goalkeepers.server.entity.GoalShare;
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
                                Goal shareGoal = null;
                                GoalShare share = goal.getShare();
                                if(Objects.nonNull(share)) {
                                    shareGoal = Optional.ofNullable(share.getGoal()).orElse(null);
                                }
                                // shareGoal에 대한 joinMemberList 가져오기
                                List<Map<String, Object>> joinMemberList = new ArrayList<>();
                                if(Objects.nonNull(shareGoal)) {
                                    for(GoalShare goalShare : shareGoal.getShareList()) {
                                        Map<String, Object> member = new HashMap<>();
                                        member.put("memberId", goalShare.getMember());
                                        member.put("nickname", goalShare.getMember().getNickname());
                                        joinMemberList.add(member);
                                    }
                                }
                                return GoalResponseDto.of(goal, imageUrl, joinMemberList);
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
