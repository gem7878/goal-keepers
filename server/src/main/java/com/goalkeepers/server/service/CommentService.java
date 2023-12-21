package com.goalkeepers.server.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.goalkeepers.server.config.SecurityUtil;
import com.goalkeepers.server.dto.CommentRequestDto;
import com.goalkeepers.server.dto.CommentResponseDto;
import com.goalkeepers.server.entity.Member;
import com.goalkeepers.server.entity.Post;
import com.goalkeepers.server.entity.PostComment;
import com.goalkeepers.server.repository.CommentRepository;
import com.goalkeepers.server.repository.MemberRepository;
import com.goalkeepers.server.repository.PostRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class CommentService {
    
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
        Post post = postRepository.findById(postId)
                    .orElseThrow(() -> new RuntimeException("Post Id를 확인해주세요."));
        
        return commentRepository.findAllByPost(post)
                                .stream()
                                .map(CommentResponseDto::of)
                                .collect(Collectors.toList());
    }

    public CommentResponseDto createMyComment(CommentRequestDto requestDto, Long postId) {
        Member member = isMemberCurrent();
        Post post = postRepository.findById(postId)
                    .orElseThrow(() -> new RuntimeException("Post Id를 확인해주세요."));
        
        PostComment comment = requestDto.toComment(member, post);
        return CommentResponseDto.of(commentRepository.save(comment)); 
    }

    public CommentResponseDto updateMyComment(CommentRequestDto requestDto, Long commentId) {
        Member member = isMemberCurrent();
        PostComment comment = commentRepository.findById(commentId)
                    .orElseThrow(() -> new RuntimeException("Post Id를 확인해주세요."));
        
        if(comment.getMember().equals(member)) {
            return CommentResponseDto.of(PostComment.commentUpdate(comment, requestDto));
        } else {
            throw new RuntimeException("로그인한 유저와 작성 유저가 같지 않습니다.");
        }
    }

    public void deleteMyComment(Long commentId) {
        Member member = isMemberCurrent();
        PostComment comment = commentRepository.findById(commentId)
                    .orElseThrow(() -> new RuntimeException("Post Id를 확인해주세요."));
        
        if(comment.getMember().equals(member)) {
            commentRepository.delete(comment);
        } else {
            throw new RuntimeException("로그인한 유저와 작성 유저가 같지 않습니다.");
        }
    }

    // 로그인 했는지 확인
    public Member isMemberCurrent() {
        return memberRepository.findById(SecurityUtil.getCurrentMemberId())
                .orElseThrow(() -> new RuntimeException("로그인 유저 정보가 없습니다"));
    }
}
