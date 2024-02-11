package com.goalkeepers.server.repository;

import static com.goalkeepers.server.entity.QPostContent.postContent;
import static com.goalkeepers.server.entity.QGoal.goal;
import static com.goalkeepers.server.entity.QPost.post;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.goalkeepers.server.common.CommonUtils;
import com.goalkeepers.server.config.SecurityUtil;
import com.goalkeepers.server.dto.PostContentResponseDto;
import com.goalkeepers.server.dto.PostResponseDto;
import com.goalkeepers.server.dto.TargetResponseDto;
import com.goalkeepers.server.entity.Goal;
import com.goalkeepers.server.entity.Member;
import com.goalkeepers.server.entity.Post;
import com.goalkeepers.server.entity.PostContent;
import com.goalkeepers.server.entity.SORT;
import com.goalkeepers.server.entity.TYPE;
import com.goalkeepers.server.service.FirebaseStorageService;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Repository
public class PostContentRepositoryImpl implements PostContentRepositoryCustom {

    private final JPAQueryFactory queryFactory;
    private EntityManager entityManager;
    private final FirebaseStorageService firebaseStorageService;
    private final GoalShareRepository shareRepository;
    private final MemberRepository memberRepository;
    private final PostLikeRepository likeRepository;
    private final PostCheerRepository cheerRepository;

    @Override
    public Page<PostResponseDto> getAllContentAndGoal(Pageable pageable, SORT sort) {

        boolean isCheerSort = SORT.CHEER.equals(sort);
        
        List<PostResponseDto> page = null;
        int totalSize = 0;

        if(isCheerSort) {
            List<Post> posts = queryFactory
                                .selectFrom(post)
                                .orderBy(post.cheerCnt.desc(), post.id.desc())
                                .offset(pageable.getOffset())
                                .limit(pageable.getPageSize())
                                .fetch();

            page = posts
                .stream()
                .map(post -> {
                    Member member = CommonUtils.MemberOrNull(memberRepository);
                    Goal goal = post.getGoal();
                    PostContent content = queryFactory
                                            .selectFrom(postContent)
                                            .where(postContent.post.eq(post))
                                            .orderBy(postContent.createdAt.desc())
                                            .limit(1)
                                            .fetchOne();

                    String imageUrl = CommonUtils.getImageUrl(goal, firebaseStorageService);
                    boolean isShare = CommonUtils.isShareGoal(goal, member, shareRepository);
                    boolean isCheer = CommonUtils.isCheerPost(post, member, cheerRepository);
                    boolean isMyPost = goal.getMember().equals(member);
                    int goalShareCnt = CommonUtils.getOriginalGoalShareCnt(goal);
                    PostContentResponseDto contentResponseDto = PostContentResponseDto.of(
                                                                content, 
                                                                null, 
                                                                member.getNickname(), 
                                                                CommonUtils.isLikeContent(content, member, likeRepository), 
                                                                null);
                                                                
                    return PostResponseDto.of(post, member, isCheer, isMyPost, goal, imageUrl, isShare, goalShareCnt, contentResponseDto);
                }).collect(Collectors.toList());

            totalSize = queryFactory
                        .selectFrom(post)
                        .fetch()
                        .size();
        } else {
            boolean isNewSort = SORT.NEW.equals(sort); // LIKE OR NEW

            List<PostContent> contents = queryFactory
                                        .selectFrom(postContent)
                                        .orderBy(isNewSort ? postContent.createdAt.desc() : postContent.likeCnt.desc())
                                        .orderBy(isNewSort ? postContent.likeCnt.desc() : postContent.createdAt.desc())
                                        .offset(pageable.getOffset())
                                        .limit(pageable.getPageSize())
                                        .fetch();
            
            page = contents
                .stream()
                .map(content -> {
                    Member member = CommonUtils.MemberOrNull(memberRepository);
                    Goal goal = content.getPost().getGoal();
                    PostContentResponseDto contentResponseDto = PostContentResponseDto.of(
                                                                content,
                                                                null, 
                                                                content.getMember().getNickname(), 
                                                                CommonUtils.isLikeContent(content, member, likeRepository), 
                                                                null);
                    String imageUrl = CommonUtils.getImageUrl(goal, firebaseStorageService);
                    boolean isShare = CommonUtils.isShareGoal(goal, member, shareRepository);
                    boolean isCheer = CommonUtils.isCheerPost(content.getPost(), member, cheerRepository);
                    boolean isMyPost = goal.getMember().equals(member);
                    int goalShareCnt = CommonUtils.getOriginalGoalShareCnt(goal);
                    return PostResponseDto.of(content.getPost(), member, isCheer, isMyPost, goal, imageUrl, isShare, goalShareCnt, contentResponseDto);
                }).collect(Collectors.toList());

            totalSize = queryFactory
                            .selectFrom(postContent)
                            .fetch()
                            .size();
        }
        
        return new PageImpl<>(page, pageable, totalSize);
    }
    @Override
    public Page<PostResponseDto> getMyAllContentAndGoal(Pageable pageable, Member member) {

        List<Tuple> tuples = queryFactory
                        .select(postContent.post, postContent.id.max())
                        .from(postContent)
                        .where(postContent.member.eq(member))
                        .groupBy(postContent.post)
                        .offset(pageable.getOffset())
                        .limit(pageable.getPageSize())
                        .fetch();

        List<PostResponseDto> page = tuples
            .stream()
            .map(tuple -> {

                Post post = tuple.get(postContent.post);
                Goal goal = post.getGoal();
                PostContent content = queryFactory
                                    .selectFrom(postContent)
                                    .where(postContent.id.eq(tuple.get(postContent.id.max())))
                                    .limit(1)
                                    .fetchOne();

                String imageUrl = CommonUtils.getImageUrl(goal, firebaseStorageService);
                PostContentResponseDto contentResponseDto = PostContentResponseDto.of(
                                                            content, 
                                                            null, 
                                                            content.getMember().getNickname(), 
                                                            CommonUtils.isLikeContent(content, member, likeRepository), 
                                                            null);
                boolean isCheer = CommonUtils.isCheerPost(post, member, cheerRepository);
                int goalShareCnt = CommonUtils.getOriginalGoalShareCnt(goal);
                return PostResponseDto.of(post, member, isCheer, true, goal, imageUrl, false, goalShareCnt, contentResponseDto);
            }).collect(Collectors.toList());

        int totalSize = queryFactory
                        .selectFrom(postContent)
                        .where(postContent.member.eq(member))
                        .fetch()
                        .size();
        
        return new PageImpl<>(page, pageable, totalSize);
    }
    @Override
    public Page<PostContentResponseDto> getPostContents(Pageable pageable, Post post) {
        List<PostContent> contents = queryFactory
                                    .selectFrom(postContent)
                                    .where(postContent.post.eq(post))
                                    .orderBy(postContent.createdAt.desc())
                                    .offset(pageable.getOffset())
                                    .limit(pageable.getPageSize())
                                    .fetch();
        
        List<PostContentResponseDto> page = contents
            .stream()
            .map(content -> {
                Goal goal = content.getPost().getGoal();
                return PostContentResponseDto.of(
                        content,
                        goal,
                        content.getMember().getNickname(), 
                        CommonUtils.isLikeContent(content, CommonUtils.MemberOrNull(memberRepository), likeRepository), 
                        null); // CommonUtils.getImageUrl(goal, firebaseStorageService)
            }).collect(Collectors.toList());

        int totalSize = queryFactory
                        .selectFrom(postContent)
                        .where(postContent.post.eq(post))
                        .fetch()
                        .size();
        
        return new PageImpl<>(page, pageable, totalSize);
    }
    @Override
    public Page<PostResponseDto> searchPost(Pageable pageable, String query, SORT sort) {

        boolean isNewSort = SORT.NEW.equals(sort); // LIKE OR NEW

        List<Tuple> tuples = queryFactory
                            .select(post.id, post.goal.id, postContent.id.max())
                            .from(post)
                            .rightJoin(postContent).on(post.id.eq(postContent.post.id))
                            .where(postContent.content.contains(query)
                                .or(post.goal.title.contains(query))
                                .or(post.goal.description.contains(query)))
                            .groupBy(post.id, post.goal.id)
                            .orderBy(isNewSort ? postContent.id.max().desc() : postContent.likeCnt.max().desc(),
                                    post.id.desc())
                            .fetch();

        List<PostResponseDto> page = tuples
            .stream()
            .map(tuple -> {
                Goal oneGoal = queryFactory
                                    .selectFrom(goal)
                                    .where(goal.id.eq(tuple.get(post.goal.id)))
                                    .limit(1)
                                    .fetchOne();

                PostContent content = queryFactory
                                    .selectFrom(postContent)
                                    .where(postContent.id.eq(tuple.get(postContent.id.max())))
                                    .limit(1)
                                    .fetchOne();

                Member member = CommonUtils.MemberOrNull(memberRepository);
                
                PostContentResponseDto contentResponseDto = PostContentResponseDto.of(
                                                            content, 
                                                            null, 
                                                            content.getMember().getNickname(), 
                                                            CommonUtils.isLikeContent(content, member, likeRepository), 
                                                            null);
                Post onePost = queryFactory
                            .selectFrom(post)
                            .where(post.id.eq(tuple.get(post.id)))
                            .fetchFirst();
                boolean isMyPost = oneGoal.getMember().equals(member);
                int goalShareCnt = CommonUtils.getOriginalGoalShareCnt(oneGoal);
                return PostResponseDto.of(
                    onePost,
                    member,
                    CommonUtils.isCheerPost(onePost, member, cheerRepository),
                    isMyPost,
                    oneGoal, 
                    CommonUtils.getImageUrl(oneGoal, firebaseStorageService), 
                    CommonUtils.isShareGoal(oneGoal, member, shareRepository), 
                    goalShareCnt,
                    contentResponseDto);

            }).collect(Collectors.toList());

        int totalSize = queryFactory
                        .selectFrom(postContent)
                        .where(postContent.post.eq(post))
                        .fetch()
                        .size();
        
        return new PageImpl<>(page, pageable, totalSize);
    }
    @Override
    public Page<PostContentResponseDto> getCommunityContents(Pageable pageable, Goal goal) {
        List<PostContent> contents = queryFactory
                                    .selectFrom(postContent)
                                    .where(postContent.shareGoal.eq(goal))
                                    .orderBy(postContent.createdAt.desc())
                                    .offset(pageable.getOffset())
                                    .limit(pageable.getPageSize())
                                    .fetch();
        
        Member member = CommonUtils.MemberOrNull(memberRepository);
        List<PostContentResponseDto> page = contents
            .stream()
            .map(content -> PostContentResponseDto.of(
                            content, 
                            content.getPost().getGoal(), 
                            content.getMember().getNickname(), 
                            CommonUtils.isLikeContent(content, member, likeRepository), 
                            null))
            .collect(Collectors.toList());

        int totalSize = queryFactory
                        .selectFrom(postContent)
                        .where(postContent.shareGoal.eq(goal))
                        .fetch()
                        .size();

        return new PageImpl<>(page, pageable, totalSize);
    }
    @SuppressWarnings("unchecked")
    @Override
    public TargetResponseDto findTarget(TYPE type, Long targetId, Long commentId) {
        Long memberId = SecurityUtil.getCurrentMemberId();
        Integer targetPage = null;
        Integer commentPage = null;
        String targetQuery = null;
        String commentQuery = null;

        switch (type) {
            case COMMENT:
                targetQuery = postQuery(targetId, memberId);
                commentQuery = commentQuery(targetId, commentId);
                break;
            case LIKE:
            case CHEER:
                targetQuery = postQuery(targetId, memberId);
                break;
            case SHARE:
                targetQuery = null;
                break;
            case NOTIFY:
                targetQuery = null;
            default:
                break;
        }

        if(Objects.nonNull(targetQuery)) {
            Query targetResult = entityManager.createNativeQuery(targetQuery);
            targetResult.setMaxResults(1);
            List<Integer> targetResultList = targetResult.getResultList();
            Integer index = targetResultList.isEmpty() ? null : targetResultList.get(0);
            targetPage = (index / 20) + 1;
        }
        
        
        if(Objects.nonNull(commentQuery)) {
            Query commentResult = entityManager.createNativeQuery(commentQuery);
            commentResult.setMaxResults(1);
            List<Integer> commentResultList = commentResult.getResultList();
            Integer commentIndex = commentResultList.isEmpty() ? null : commentResultList.get(0);
            commentPage = (commentIndex / 20) + 1;
        }

        return TargetResponseDto.of(targetId, targetPage, commentId, commentPage);

    }
    
    private String postQuery(Long targetId, Long memberId) {
        return "SELECT index " +
            "FROM (SELECT *, ROW_NUMBER() OVER () AS index " +
            "FROM (SELECT content_tb.post_id, post_tb.goal_id, MAX(content_tb.content_id) " +
            "FROM content_tb " +
            "JOIN post_tb ON post_tb.post_id = content_tb.post_id " +
            "JOIN goal_tb ON post_tb.goal_id = goal_tb.goal_id " +
            "WHERE content_tb.member_id = " + memberId +
            "GROUP BY content_tb.post_id, post_tb.goal_id " +
            "ORDER BY MAX(content_tb.content_id) DESC) AS foo) AS soo " +
            "WHERE soo.post_id = " + targetId +
            "LIMIT 1";
    }

    private String commentQuery(Long targetId, Long commentId) {
        return "SELECT index " +
                "FROM (SELECT ROW_NUMBER() OVER () AS index, * " +
                "FROM comment_tb " +
                "WHERE post_id = " + targetId +
                "ORDER BY comment_id asc) AS foo " +
                "WHERE comment_id = " + commentId +
                "LIMIT 1";
    }
}