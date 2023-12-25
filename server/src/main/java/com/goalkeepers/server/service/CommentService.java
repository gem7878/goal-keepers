package com.goalkeepers.server.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.goalkeepers.server.config.SecurityUtil;
import com.goalkeepers.server.dto.CommentRequestDto;
import com.goalkeepers.server.dto.CommentResponseDto;
import com.goalkeepers.server.dto.PostListPageResponseDto;
import com.goalkeepers.server.entity.Member;
import com.goalkeepers.server.entity.Post;
import com.goalkeepers.server.entity.PostComment;
import com.goalkeepers.server.exception.CustomException;
import com.goalkeepers.server.repository.CommentRepository;
import com.goalkeepers.server.repository.MemberRepository;
import com.goalkeepers.server.repository.PostRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class CommentService extends CommonService {
    
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final MemberRepository memberRepository;

    /*
     *  게시글 상세보기 (전체 댓글 보기)
        댓글 쓰기
        댓글 삭제
        댓글 수정
     */

    public List<CommentResponseDto> getSelectedPost(Long postId) {
        Long memberId = SecurityUtil.getCurrentMemberId();
        Member member = memberId != null ?
                memberRepository.findById(memberId).orElseThrow(() -> new CustomException("member id 오류")) :
                null;
    
        List<PostComment> comments = commentRepository.findAllByPost(isPost(postRepository, postId));
        return comments.stream()
                .map(comment -> CommentResponseDto.of(comment, writeCommentMember(comment, member)))
                .collect(Collectors.toList());
    }
    
    private boolean writeCommentMember(PostComment comment, Member member) {
        return member != null && comment.getMember().equals(member);
    }

    public CommentResponseDto createMyComment(CommentRequestDto requestDto, Long postId) {
        Member member = isMemberCurrent(memberRepository);
        Post post = isPost(postRepository, postId);
        
        PostComment comment = requestDto.toComment(member, post);
        return CommentResponseDto.of(commentRepository.save(comment), true); 
    }

    public CommentResponseDto updateMyComment(CommentRequestDto requestDto, Long commentId) {
        PostComment comment = isMyComment(memberRepository, commentRepository, commentId);
        return CommentResponseDto.of(PostComment.commentUpdate(comment, requestDto), true);
    }

    public void deleteMyComment(Long commentId) {
        PostComment comment = isMyComment(memberRepository, commentRepository, commentId);
        commentRepository.delete(comment);
    }
}
