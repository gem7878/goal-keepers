package com.goalkeepers.server.repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.goalkeepers.server.config.SecurityUtil;
import com.goalkeepers.server.dto.PostListPageResponseDto;
import com.goalkeepers.server.entity.Member;
import com.goalkeepers.server.entity.Post;
import com.querydsl.jpa.impl.JPAQueryFactory;

import static com.goalkeepers.server.entity.QPost.post;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class PostRepositoryImpl implements PostRepositoryCustom {

    private final JPAQueryFactory queryFactory;
    private final MemberRepository memberRepository;
    private final PostLikeRepository likeRepository;
    private final GoalShareRepository shareRepository;

    @Override
    public Page<PostListPageResponseDto> searchAll(Pageable pageable) {

        List<Post> posts = queryFactory
                            .selectFrom(post)
                            .orderBy(post.id.desc())
                            .offset(pageable.getOffset())
                            .limit(pageable.getPageSize())
                            .fetch();
        
        Long memberId = SecurityUtil.getCurrentMemberId();
        List<PostListPageResponseDto> page;

        if (memberId == null) {
            page = posts
                    .stream()
                    .map(post -> PostListPageResponseDto.of(post, false, false))
                    .collect(Collectors.toList());
        } else {
            Optional<Member> member = memberRepository.findById(memberId);
            page = posts
                    .stream()
                    .map(post -> {
                        boolean isLike = likeRepository.existsByMemberAndPost(member.get(), post);
                        boolean isShare = post.getGoal() != null ? shareRepository.existsByMemberAndGoal(member.get(), post.getGoal()) : false;
                        return PostListPageResponseDto.of(post, isLike, isShare);
                    })
                    .collect(Collectors.toList());
        }
        
        
        int totalSize = queryFactory
                        .selectFrom(post)
                        .fetch()
                        .size();

        return new PageImpl<>(page, pageable, totalSize);
    }
}