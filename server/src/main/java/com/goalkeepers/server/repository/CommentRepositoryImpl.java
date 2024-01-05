package com.goalkeepers.server.repository;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.goalkeepers.server.config.SecurityUtil;
import com.goalkeepers.server.dto.CommentResponseDto;
import com.goalkeepers.server.entity.Member;
import com.goalkeepers.server.entity.PostComment;
import com.goalkeepers.server.exception.CustomException;
import com.querydsl.jpa.impl.JPAQueryFactory;

import static com.goalkeepers.server.entity.QPostComment.postComment;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class CommentRepositoryImpl implements CommentRepositoryCustom {
    
    private final JPAQueryFactory queryFactory;
    private final MemberRepository memberRepository;

    @Override
    public Page<CommentResponseDto> searchAllwithPost(Pageable pageable, Long postId) {

        List<PostComment> comments = queryFactory
                                    .selectFrom(postComment)
                                    .where(postComment.post.id.eq(postId))
                                    .orderBy(postComment.id.asc())
                                    .offset(pageable.getOffset())
                                    .limit(pageable.getPageSize())
                                    .fetch();
        
        Long memberId = SecurityUtil.getCurrentMemberId();
        Member member = memberId != null ?
                memberRepository.findById(memberId).orElseThrow(() -> new CustomException("유저 오류")) :
                null;
        
        List<CommentResponseDto> page = comments
                    .stream()
                    .map(comment -> CommentResponseDto.of(comment, writeCommentMember(comment, member)))
                    .collect(Collectors.toList());

        int totalSize = queryFactory
                        .selectFrom(postComment)
                        .where(postComment.post.id.eq(postId))
                        .fetch()
                        .size();

        return new PageImpl<>(page, pageable, totalSize);   
    }

    private boolean writeCommentMember(PostComment comment, Member member) {
        return member != null && comment.getMember().equals(member);
    }
}