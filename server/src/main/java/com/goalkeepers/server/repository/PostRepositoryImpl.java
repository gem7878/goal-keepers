package com.goalkeepers.server.repository;

import java.util.Objects;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.goalkeepers.server.config.SecurityUtil;
import com.goalkeepers.server.dto.PostContentResponseDto;
import com.goalkeepers.server.dto.PostMyResponseDto;
import com.goalkeepers.server.dto.PostResponseDto;
import com.goalkeepers.server.entity.Goal;
import com.goalkeepers.server.entity.GoalShare;
import com.goalkeepers.server.entity.Member;
import com.goalkeepers.server.entity.Post;
import com.goalkeepers.server.entity.PostContent;
import com.goalkeepers.server.service.FirebaseStorageService;
import com.querydsl.jpa.impl.JPAQueryFactory;

import static com.goalkeepers.server.entity.QPost.post;
import static com.goalkeepers.server.entity.QPostContent.postContent;
import static com.goalkeepers.server.entity.QGoal.goal;
import lombok.RequiredArgsConstructor;

@Repository
public class PostRepositoryImpl implements PostRepositoryCustom {

    private final JPAQueryFactory queryFactory;
    private final MemberRepository memberRepository;
    private final PostLikeRepository likeRepository;
    private final GoalShareRepository shareRepository;
    private final FirebaseStorageService firebaseStorageService;
    private final Pageable contentPageable;

    public PostRepositoryImpl(
            JPAQueryFactory queryFactory,
            MemberRepository memberRepository,
            PostLikeRepository likeRepository,
            GoalShareRepository shareRepository,
            FirebaseStorageService firebaseStorageService) {
        this.queryFactory = queryFactory;
        this.memberRepository = memberRepository;
        this.likeRepository = likeRepository;
        this.shareRepository = shareRepository;
        this.firebaseStorageService = firebaseStorageService;
        this.contentPageable = PageRequest.of(1 - 1, 20); // 일단 최신 20개만 보여줌
    }

    @Override
    public Page<PostResponseDto> getAll(Pageable pageable) {

        List<Post> posts = queryFactory
                            .selectFrom(post)
                            .orderBy(post.id.desc())
                            .offset(pageable.getOffset())
                            .limit(pageable.getPageSize())
                            .fetch();
        
        Long memberId = SecurityUtil.getCurrentMemberId();
        List<PostResponseDto> page = posts
            .stream()
            .map(post -> {
                Goal originalGoal = post.getOriginalGoal();

                String originalGoalImageUrl = null;
                if(Objects.nonNull(originalGoal) && Objects.nonNull(originalGoal.getImageUrl())) {
                    originalGoalImageUrl = firebaseStorageService.showFile(originalGoal.getImageUrl());
                }

                boolean isShare = false;
                Member member = memberRepository.findById(memberId).orElse(null);
                if(Objects.nonNull(member)) {
                    isShare = shareRepository.existsByMemberAndGoal(member, originalGoal);
                }

                // joinMemberList 가져오기
                List<Map<String, Object>> joinMemberList = new ArrayList<>();
                if(Objects.nonNull(originalGoal)) {
                    for(GoalShare goalShare : originalGoal.getShareList()) {
                        Map<String, Object> joinMember = new HashMap<>();
                        joinMember.put("memberId", goalShare.getMember().getId());
                        joinMember.put("nickname", goalShare.getMember().getNickname());
                        joinMemberList.add(joinMember);
                    }
                }
                
                // PostContent 가져오기
                List<PostContent> contents = queryFactory
                                                .selectFrom(postContent)
                                                .where(postContent.post.eq(post))
                                                .orderBy(postContent.id.desc())
                                                .offset(contentPageable.getOffset())
                                                .limit(contentPageable.getPageSize())
                                                .fetch();
                                            
                List<PostContentResponseDto> contentList = contents
                                                .stream()
                                                .map(content -> {
                                                    boolean isLike = false;
                                                    if(Objects.nonNull(member)) {
                                                        isLike = likeRepository.existsByMemberAndPostContent(member, content);
                                                    }
                                                    return PostContentResponseDto.of(content, content.getShareGoal(), content.getMember().getNickname(), isLike, null);
                                                }).collect(Collectors.toList());

                return PostResponseDto.of(post.getId(), post.getOriginalGoal(), originalGoalImageUrl, isShare, joinMemberList, contentList);
            }).collect(Collectors.toList());
        
        int totalSize = queryFactory
                        .selectFrom(post)
                        .fetch()
                        .size();

        return new PageImpl<>(page, pageable, totalSize);
    }

    @Override
    public Page<PostMyResponseDto> getMyAllPost(Pageable pageable, Member member) {

        // 나의 post 찾기
        List<Goal> goals = queryFactory
                    .selectFrom(goal)
                    .where(goal.member.eq(member))
                    .offset(pageable.getOffset())
                    .limit(pageable.getPageSize())
                    .fetch();

        List<PostMyResponseDto> page = goals
            .stream()
            .map(goal -> {
                List<PostContent> contents = queryFactory
                                                .selectFrom(postContent)
                                                .where(postContent.shareGoal.eq(goal))
                                                .orderBy(postContent.id.desc())
                                                .offset(contentPageable.getOffset())
                                                .limit(contentPageable.getPageSize())
                                                .fetch();
                
                if(Objects.isNull(contents)) {
                    return null;
                }

                Goal originalGoal = goal;
                boolean isShare = false;
                GoalShare share = goal.getShare();
                if(Objects.nonNull(share)) {
                    originalGoal = Objects.nonNull(share.getGoal()) ? share.getGoal() : goal;
                    isShare = true;
                }

                Long postId = queryFactory
                                .select(post.id)
                                .from(post)
                                .where(post.originalGoal.eq(originalGoal))
                                .fetchOne();
                
                // joinMemberList 가져오기
                List<Map<String, Object>> joinMemberList = new ArrayList<>();
                if(Objects.nonNull(originalGoal)) {
                    for(GoalShare goalShare : originalGoal.getShareList()) {
                        Map<String, Object> joinMember = new HashMap<>();
                        joinMember.put("memberId", goalShare.getMember().getId());
                        joinMember.put("nickname", goalShare.getMember().getNickname());
                        joinMemberList.add(joinMember);
                    }
                }
                
                List<PostContentResponseDto> contentList = contents
                        .stream()
                        .map(content -> {
                            boolean isLike = false;
                            if(Objects.nonNull(member)) {
                                isLike = likeRepository.existsByMemberAndPostContent(member, content);
                            }
                            return PostContentResponseDto.of(content, null, member.getNickname(), isLike, null);
                        }).collect(Collectors.toList());
                
                String imageUrl = null;
                if(Objects.nonNull(goal) && Objects.nonNull(goal.getImageUrl())) {
                    imageUrl = firebaseStorageService.showFile(goal.getImageUrl());
                }
                
                return PostMyResponseDto.of(postId, goal, originalGoal, imageUrl, isShare, joinMemberList, contentList);
            }).collect(Collectors.toList());        

        int totalSize = queryFactory
                        .selectFrom(goal)
                        .where(goal.member.eq(member))
                        .fetch()
                        .size();

        return new PageImpl<>(page, pageable, totalSize);
    }

    @Override
    public Page<PostResponseDto> searchAll(Pageable pageable, String query, String sort) {
        
        List<Post> posts = queryFactory
                            .selectFrom(post)
                            .where(post.contentList.any().content.contains(query)
                                //.or(post.contentList.any().shareGoal.description.contains(query))
                                .or(post.originalGoal.title.contains(query)
                                .or(post.originalGoal.description.contains(query))))
                            .orderBy(sort.equals("like") ? post.contentList.any().likeCnt.desc() : post.contentList.any().updatedAt.desc())
                            .offset(pageable.getOffset())
                            .limit(pageable.getPageSize())
                            .fetch();
        
        Long memberId = SecurityUtil.getCurrentMemberId();
        List<PostResponseDto> page = posts
            .stream()
            .map(post -> {
                Goal originalGoal = post.getOriginalGoal();

                String originalGoalImageUrl = null;
                if(Objects.nonNull(originalGoal) && Objects.nonNull(originalGoal.getImageUrl())) {
                    originalGoalImageUrl = firebaseStorageService.showFile(originalGoal.getImageUrl());
                }

                boolean isShare = false;
                Member member = memberRepository.findById(memberId).orElse(null);
                if(Objects.nonNull(member)) {
                    isShare = shareRepository.existsByMemberAndGoal(member, originalGoal);
                }

                // joinMemberList 가져오기
                List<Map<String, Object>> joinMemberList = new ArrayList<>();
                if(Objects.nonNull(originalGoal)) {
                    for(GoalShare goalShare : originalGoal.getShareList()) {
                        Map<String, Object> joinMember = new HashMap<>();
                        joinMember.put("memberId", goalShare.getMember().getId());
                        joinMember.put("nickname", goalShare.getMember().getNickname());
                        joinMemberList.add(joinMember);
                    }
                }
                
                // PostContent 가져오기
                List<PostContent> contents = queryFactory
                                                .selectFrom(postContent)
                                                .where(postContent.post.eq(post))
                                                .orderBy(postContent.id.desc())
                                                .offset(contentPageable.getOffset())
                                                .limit(contentPageable.getPageSize())
                                                .fetch();
                                            
                List<PostContentResponseDto> contentList = contents
                                                .stream()
                                                .map(content -> {
                                                    boolean isLike = false;
                                                    if(Objects.nonNull(member)) {
                                                        isLike = likeRepository.existsByMemberAndPostContent(member, content);
                                                    }
                                                    String imageUrl = null;
                                                    if(Objects.nonNull(content.getShareGoal()) && Objects.nonNull(content.getShareGoal().getImageUrl())) {
                                                        imageUrl = firebaseStorageService.showFile(content.getShareGoal().getImageUrl());
                                                    }
                                                    return PostContentResponseDto.of(content, content.getShareGoal(), content.getMember().getNickname(), isLike, imageUrl);
                                                }).collect(Collectors.toList());

                return PostResponseDto.of(post.getId(), post.getOriginalGoal(), originalGoalImageUrl, isShare, joinMemberList, contentList);
            }).collect(Collectors.toList());

        int totalSize = queryFactory
                        .selectFrom(post)
                        .where(post.contentList.any().content.contains(query)
                                //.or(post.contentList.any().shareGoal.description.contains(query))
                                .or(post.originalGoal.title.contains(query)
                                .or(post.originalGoal.description.contains(query))))
                        .fetch()
                        .size();

        return new PageImpl<>(page, pageable, totalSize);
    }

    @Override
    public PostResponseDto getOnePost(Pageable pageable, Post post) {
        Member member = memberRepository.findById(SecurityUtil.getCurrentMemberId()).orElse(null);
        boolean isShare = false;
        // PostContent 가져오기
        List<PostContent> contents = queryFactory
                            .selectFrom(postContent)
                            .where(postContent.post.eq(post))
                            .orderBy(postContent.id.desc())
                            .offset(contentPageable.getOffset())
                            .limit(contentPageable.getPageSize())
                            .fetch();

        Goal originalGoal = post.getOriginalGoal();
        if(Objects.nonNull(member)) {
            isShare = shareRepository.existsByMemberAndGoal(member, originalGoal);
        }

        String originalGoalImageUrl = null;
        if(Objects.nonNull(originalGoal) && Objects.nonNull(originalGoal.getImageUrl())) {
            originalGoalImageUrl = firebaseStorageService.showFile(originalGoal.getImageUrl());
        }

        List<PostContentResponseDto> contentList = contents
                            .stream()
                            .map(content -> {
                                boolean isLike = false;
                                if(Objects.nonNull(member)) {
                                    isLike = likeRepository.existsByMemberAndPostContent(member, content);
                                }
                                return PostContentResponseDto.of(content, content.getShareGoal(), content.getMember().getNickname(), isLike, null);
                            }).collect(Collectors.toList());
        
        // joinMemberList 가져오기
        List<Map<String, Object>> joinMemberList = new ArrayList<>();
        if(Objects.nonNull(originalGoal)) {
            for(GoalShare goalShare : originalGoal.getShareList()) {
                Map<String, Object> joinMember = new HashMap<>();
                joinMember.put("memberId", goalShare.getMember().getId());
                joinMember.put("nickname", goalShare.getMember().getNickname());
                joinMemberList.add(joinMember);
            }
        }
        return PostResponseDto.of(post.getId(), originalGoal, originalGoalImageUrl, isShare, joinMemberList, contentList);
    }
}