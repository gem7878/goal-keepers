package com.goalkeepers.server.repository;


import static com.goalkeepers.server.entity.QMember.member;
import static com.goalkeepers.server.entity.QPost.post;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.goalkeepers.server.common.RepositoryHelper;
import com.goalkeepers.server.dto.PostSelectResponseDto;
import com.goalkeepers.server.entity.Goal;
import com.goalkeepers.server.entity.Post;
import com.goalkeepers.server.service.FirebaseStorageService;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Repository
public class PostRepositoryImpl implements PostRepositoryCustom {

    private final JPAQueryFactory queryFactory;
    private final FirebaseStorageService firebaseStorageService;
    
    @Override
    public Page<PostSelectResponseDto> getSelectedAllPost(Pageable pageable, Long currentMemberId) {
        List<Post> posts = queryFactory
                            .selectFrom(post)
                            .leftJoin(post.goal.member, member)
                            .where(member.id.eq(currentMemberId))
                            .orderBy(post.id.desc())
                            .offset(pageable.getOffset())
                            .limit(pageable.getPageSize())
                            .fetch();

        List<PostSelectResponseDto> page = posts
            .stream()
            .map(post -> {
                Goal goal = post.getGoal();
                String imageUrl = RepositoryHelper.getImageUrl(goal, firebaseStorageService);
                return PostSelectResponseDto.of(goal, post, imageUrl);
            })
            .collect(Collectors.toList());

        int totalSize = queryFactory
                        .selectFrom(post)
                        .leftJoin(post.goal.member, member)
                        .where(member.id.eq(currentMemberId))
                        .fetch()
                        .size();
        
        return new PageImpl<>(page, pageable, totalSize);
    }

}
