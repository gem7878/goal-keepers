package com.goalkeepers.server.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.goalkeepers.server.dto.CommentRequestDto;
import com.goalkeepers.server.dto.CommentResponseDto;
import com.goalkeepers.server.entity.Member;
import com.goalkeepers.server.entity.Post;
import com.goalkeepers.server.entity.PostComment;
import com.goalkeepers.server.entity.TYPE;
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
    private final NotificationService notificationService;
    /*
     *  게시글 상세보기 (전체 댓글 보기)
        댓글 쓰기
        댓글 삭제
        댓글 수정
     */

    public Page<CommentResponseDto> getSelectedPost(Long postId, int pageNumber) {
        isPost(postRepository, postId);
        return commentRepository.searchAllwithPost(
                PageRequest.of(
                    pageNumber - 1, 20), postId);
    }

    public CommentResponseDto createMyComment(CommentRequestDto requestDto, Long postId) {
        Member member = isMemberCurrent(memberRepository);
        Post post = isPost(postRepository, postId);
        
        PostComment comment = commentRepository.save(requestDto.toComment(member, post));
        
        Member receiver = memberRepository.findById(post.getGoal().getMember().getId()).orElseThrow(() -> new CustomException("post.getGoal.getMember.getId를 찾지 못하였습니다."));
        notificationService.send(receiver, TYPE.COMMENT, member.getNickname() + "님이 댓글을 작성하였습니다.", comment.getId());
        return CommentResponseDto.of(comment, true); 
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
