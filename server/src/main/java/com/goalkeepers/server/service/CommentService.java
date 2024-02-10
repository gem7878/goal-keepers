package com.goalkeepers.server.service;

import java.util.Objects;
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
import com.goalkeepers.server.repository.CommentRepository;
import com.goalkeepers.server.repository.MemberRepository;
import com.goalkeepers.server.repository.PostRepository;
import com.goalkeepers.server.repository.SettingRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class CommentService extends CommonService {
    
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final MemberRepository memberRepository;
    private final SettingRepository settingRepository;
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

    public Long createMyComment(CommentRequestDto requestDto, Long postId) {
        Member member = isMemberCurrent(memberRepository);
        Post post = isPost(postRepository, postId);
        
        PostComment comment = commentRepository.save(requestDto.toComment(member, post));
        
        // 알림 보내기
        Member receiver = alarmTrueReceiver(settingRepository, post.getGoal().getMember(), TYPE.COMMENT);
        if(Objects.nonNull(receiver) && !member.equals(receiver)) {
            notificationService.send(receiver, member, TYPE.COMMENT, post.getId(), post.getGoal().getTitle(), null, comment.getId());
        }
        return comment.getId();
    }

    public void updateMyComment(CommentRequestDto requestDto, Long commentId) {
        PostComment comment = isMyComment(memberRepository, commentRepository, commentId);
        PostComment.commentUpdate(comment, requestDto);
    }

    public void deleteMyComment(Long commentId) {
        PostComment comment = isMyComment(memberRepository, commentRepository, commentId);
        commentRepository.delete(comment);
    }
}
