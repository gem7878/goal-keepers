package com.goalkeepers.server.repository;

import java.util.Objects;
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
import com.goalkeepers.server.service.FirebaseStorageService;
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
    private final FirebaseStorageService firebaseStorageService;

    @Override
    public Page<PostListPageResponseDto> searchAll(Pageable pageable) {

        List<Post> posts = queryFactory
                            .selectFrom(post)
                            .orderBy(post.id.desc())
                            .offset(pageable.getOffset())
                            .limit(pageable.getPageSize())
                            .fetch();
        
        Long memberId = SecurityUtil.getCurrentMemberId();
        List<PostListPageResponseDto> page = posts.stream().map(post -> {
            boolean isLike = false;
            boolean isShare = false;
            String imageUrl = null;
            if (Objects.nonNull(post.getGoal()) && Objects.nonNull(post.getGoal().getImageUrl())) {
                imageUrl = firebaseStorageService.showFile(post.getGoal().getImageUrl());
                if (Objects.nonNull(memberId)) {
                    Optional<Member> member = memberRepository.findById(memberId);
                    isLike = likeRepository.existsByMemberAndPost(member.get(), post);
                    isShare = shareRepository.existsByMemberAndGoal(member.get(), post.getGoal());
                }
            }
            return PostListPageResponseDto.of(post, imageUrl, isLike, isShare);
        }).collect(Collectors.toList());
        
        int totalSize = queryFactory
                        .selectFrom(post)
                        .fetch()
                        .size();

        return new PageImpl<>(page, pageable, totalSize);
    }

    @Override
    public Page<PostListPageResponseDto> searchMyAllPost(Pageable pageable, Member member) {
        
        List<Post> posts = queryFactory
                            .selectFrom(post)
                            .where(post.member.eq(member))
                            .orderBy(post.id.desc())
                            .offset(pageable.getOffset())
                            .limit(pageable.getPageSize())
                            .fetch();
        
        List<PostListPageResponseDto> page = posts
                    .stream()
                    .map(post -> {
                        boolean isLike = likeRepository.existsByMemberAndPost(member, post);
                        String imageUrl = null;
                        if(Objects.nonNull(post.getGoal()) && Objects.nonNull(post.getGoal().getImageUrl())) {
                            imageUrl = firebaseStorageService.showFile(post.getGoal().getImageUrl());
                        }
                        return PostListPageResponseDto.of(post, imageUrl, isLike, false);
                    })
                    .collect(Collectors.toList());
        
        
        int totalSize = queryFactory
                        .selectFrom(post)
                        .where(post.member.eq(member))
                        .fetch()
                        .size();

        return new PageImpl<>(page, pageable, totalSize);
    }
}