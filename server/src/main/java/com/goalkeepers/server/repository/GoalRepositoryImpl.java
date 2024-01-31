package com.goalkeepers.server.repository;

import java.lang.Object;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Objects;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.Optional;
import java.util.stream.Collectors;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import com.goalkeepers.server.common.CommonUtils;
import com.goalkeepers.server.dto.CommunityResponseDto;
import com.goalkeepers.server.dto.GoalResponseDto;
import com.goalkeepers.server.dto.PostContentResponseDto;
import com.goalkeepers.server.entity.Goal;
import com.goalkeepers.server.entity.GoalShare;
import com.goalkeepers.server.entity.Member;
import com.goalkeepers.server.entity.PostContent;
import com.goalkeepers.server.entity.SORT;
import com.goalkeepers.server.service.FirebaseStorageService;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;

import static com.goalkeepers.server.entity.QGoal.goal;
import static com.goalkeepers.server.entity.QMember.member;
import static com.goalkeepers.server.entity.QPostContent.postContent;

@Repository
public class GoalRepositoryImpl implements GoalRepositoryCustom {
    
    private final JPAQueryFactory queryFactory;
    private final FirebaseStorageService firebaseStorageService;
    private final GoalShareRepository shareRepository;
    private final MemberRepository memberRepository;
    private final PostLikeRepository likeRepository;
    private final Pageable contentPageable;

    public GoalRepositoryImpl(
            JPAQueryFactory queryFactory,
            FirebaseStorageService firebaseStorageService,
            GoalShareRepository shareRepository,
            PostLikeRepository likeRepository,
            MemberRepository memberRepository) {

        this.queryFactory = queryFactory;
        this.firebaseStorageService = firebaseStorageService;
        this.shareRepository = shareRepository;
        this.likeRepository = likeRepository;
        this.memberRepository = memberRepository;
        this.contentPageable = PageRequest.of(1 - 1, 3); // 일단 3개만 보여줌
    }

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
                Goal shareGoal = goal;
                GoalShare share = goal.getShare();
                if(Objects.nonNull(share)) {
                    shareGoal = Optional.ofNullable(share.getGoal()).orElse(null);
                }
                // shareGoal에 대한 joinMemberList 가져오기
                List<Map<String, Object>> joinMemberList = new ArrayList<>();
                if(Objects.nonNull(shareGoal)) {
                    for(GoalShare goalShare : shareGoal.getShareList()) {
                        Map<String, Object> member = new HashMap<>();
                        member.put("memberId", goalShare.getMember().getId());
                        member.put("nickname", goalShare.getMember().getNickname());
                        joinMemberList.add(member);
                    }
                }
                System.out.println("joinMemberList: " + joinMemberList);
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

	@Override
	public Page<CommunityResponseDto> getAllNewGoal(Pageable pageable) {
		List<Goal> goals = queryFactory
                            .selectFrom(goal)
                            .orderBy(goal.id.desc())
                            .offset(pageable.getOffset())
                            .limit(pageable.getPageSize())
                            .fetch();

        List<CommunityResponseDto> page = goals
            .stream()
            .map(goal -> {

                // 이미지 URL
                String originalGoalImageUrl = CommonUtils.getImageUrl(goal, firebaseStorageService);

                // 담기했는지
                Member member = CommonUtils.MemberOrNull(memberRepository);
                boolean isShare = CommonUtils.isShareGoal(goal, member, shareRepository);

                // joinMemberList 가져오기
                List<Map<String, Object>> joinMemberList = CommonUtils.getJoinMemberList(goal);

                // PostContent 가져오기
                List<PostContent> contents = queryFactory
                                                .selectFrom(postContent)
                                                .where(postContent.shareGoal.eq(goal))
                                                .orderBy(postContent.createdAt.desc())
                                                .offset(contentPageable.getOffset())
                                                .limit(contentPageable.getPageSize())
                                                .fetch();
                                            
                List<PostContentResponseDto> contentList = contents
                                                .stream()
                                                .map(content -> PostContentResponseDto.of(
                                                                content, 
                                                                content.getPost().getGoal(), 
                                                                content.getMember().getNickname(), 
                                                                CommonUtils.isLikeContent(content, member, likeRepository), 
                                                                null))
                                                .collect(Collectors.toList());

                return CommunityResponseDto.of(goal, originalGoalImageUrl, isShare, joinMemberList, contentList, null);
            }).collect(Collectors.toList());

        int totalSize = queryFactory
                        .selectFrom(goal)
                        .fetch()
                        .size();
        
        return new PageImpl<>(page, pageable, totalSize);
	}

	@Override
	public Page<CommunityResponseDto> getAllPopularGoal(Pageable pageable) {
        LocalDateTime sixHoursAgo = LocalDateTime.now().minus(6, ChronoUnit.HOURS);

		List<Tuple> popularGoalAndCountList = queryFactory
                            .select(postContent.shareGoal, postContent.count())
                            .from(postContent)
                            .where(postContent.createdAt.after(sixHoursAgo))
                            .groupBy(postContent.shareGoal)
                            .orderBy(postContent.count().desc())
                            .fetch();
                                    

        List<CommunityResponseDto> page = popularGoalAndCountList
            .stream()
            .map(tuple -> {
                Goal goal = tuple.get(postContent.shareGoal);
                Long count = tuple.get(postContent.count());

                String originalGoalImageUrl = CommonUtils.getImageUrl(goal, firebaseStorageService);
                Member member = CommonUtils.MemberOrNull(memberRepository);
                boolean isShare = CommonUtils.isShareGoal(goal, member, shareRepository);
                List<Map<String, Object>> joinMemberList = CommonUtils.getJoinMemberList(goal);

                List<PostContent> contents = queryFactory
                                                .selectFrom(postContent)
                                                .where(postContent.shareGoal.eq(goal))
                                                .orderBy(postContent.createdAt.desc())
                                                .offset(contentPageable.getOffset())
                                                .limit(contentPageable.getPageSize())
                                                .fetch();
                List<PostContentResponseDto> contentList = contents
                                                .stream()
                                                .map(content -> PostContentResponseDto.of(
                                                                content, 
                                                                content.getPost().getGoal(), 
                                                                content.getMember().getNickname(), 
                                                                CommonUtils.isLikeContent(content, member, likeRepository), 
                                                                null))
                                                .collect(Collectors.toList());
                
                return CommunityResponseDto.of(goal, originalGoalImageUrl, isShare, joinMemberList, contentList, count);
            }).collect(Collectors.toList());

        int totalSize = queryFactory
                        .selectFrom(goal)
                        .fetch()
                        .size();
        
        return new PageImpl<>(page, pageable, totalSize);
	}

	@Override
	public Page<CommunityResponseDto> searchCommunity(Pageable pageable, String query, SORT sort) {
		
        List<Goal> goals = null;
        if(SORT.NEW.equals(sort)) {
            goals = queryFactory
                    .selectFrom(goal)
                    .where(goal.share.isNull().and(
                        goal.title.contains(query)
                        .or(goal.description.contains(query))
                        .or(goal.id.in(
                            JPAExpressions
                                .select(postContent.shareGoal.id)
                                .from(postContent)
                                .where(postContent.content.contains(query))
                        ))
                    ))
                    .orderBy(goal.id.desc())
                    .offset(pageable.getOffset())
                    .limit(pageable.getPageSize())
                    .fetch();
        } else if (SORT.POPULAR.equals(sort)) {
            goals = queryFactory
                    .selectFrom(goal)
                    .where(goal.id.in(
                            JPAExpressions
                                    .select(postContent.shareGoal.id)
                                    .from(postContent)
                                    .where(postContent.createdAt.after(LocalDateTime.now().minusHours(6))
                                        .and(postContent.content.contains(query)
                                            .or(postContent.shareGoal.title.contains(query)
                                            .or(postContent.shareGoal.description.contains(query)))))
                                    .groupBy(postContent.shareGoal.id)
                                    .orderBy(postContent.count().desc())
                    ))
                    .fetch();
        } else {
            throw new MethodArgumentTypeMismatchException(null, null, null, null, null);
        }

        List<CommunityResponseDto> page = goals
            .stream()
            .map(goal -> {

                String originalGoalImageUrl = CommonUtils.getImageUrl(goal, firebaseStorageService);
                Member member = CommonUtils.MemberOrNull(memberRepository);
                boolean isShare = CommonUtils.isShareGoal(goal, member, shareRepository);
                List<Map<String, Object>> joinMemberList = CommonUtils.getJoinMemberList(goal);
                List<PostContent> contents = queryFactory
                                                .selectFrom(postContent)
                                                .where(postContent.shareGoal.eq(goal))
                                                .orderBy(postContent.createdAt.desc())
                                                .offset(contentPageable.getOffset())
                                                .limit(contentPageable.getPageSize())
                                                .fetch();
                                            
                List<PostContentResponseDto> contentList = contents
                                                .stream()
                                                .map(content -> PostContentResponseDto.of(
                                                                content, 
                                                                content.getPost().getGoal(), 
                                                                content.getMember().getNickname(), 
                                                                CommonUtils.isLikeContent(content, member, likeRepository), 
                                                                null))
                                                .collect(Collectors.toList());

                return CommunityResponseDto.of(goal, originalGoalImageUrl, isShare, joinMemberList, contentList, null);
            }).collect(Collectors.toList());

        int totalSize = queryFactory
                        .selectFrom(goal)
                        .where(goal.title.contains(query)
                                .or(goal.description.contains(query)))
                        .fetch()
                        .size();
        
        return new PageImpl<>(page, pageable, totalSize);
	}
}
