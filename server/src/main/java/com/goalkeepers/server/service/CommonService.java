package com.goalkeepers.server.service;

import org.springframework.stereotype.Service;

import com.goalkeepers.server.config.SecurityUtil;
import com.goalkeepers.server.entity.Goal;
import com.goalkeepers.server.entity.Member;
import com.goalkeepers.server.entity.Post;
import com.goalkeepers.server.entity.PostComment;
import com.goalkeepers.server.exception.CustomException;
import com.goalkeepers.server.repository.CommentRepository;
import com.goalkeepers.server.repository.GoalRepository;
import com.goalkeepers.server.repository.MemberRepository;
import com.goalkeepers.server.repository.PostRepository;


@Service
public class CommonService {
    
    // 로그인 했는지 확인
    public Member isMemberCurrent(MemberRepository memberRepository) {
        return memberRepository.findById(SecurityUtil.getCurrentMemberId())
                .orElseThrow(() -> new CustomException("로그인 유저 정보가 없습니다."));
    }

    // 나의 Goal 찾기
    public Goal isMyGoal(MemberRepository memberRepository, GoalRepository goalRepository, Long goalId) {
        Member member = isMemberCurrent(memberRepository);
        return goalRepository.findByIdAndMember(goalId, member)
                            .orElseThrow(() -> new CustomException("나의 Goal Id가 아닙니다."));
    }

    // 나의 Post 찾기
    public Post isMyPost(MemberRepository memberRepository, PostRepository postRepository, Long postId) {
        Member member = isMemberCurrent(memberRepository);
        return postRepository.findByIdAndMember(postId, member)
                            .orElseThrow(() -> new CustomException("나의 Post Id가 아닙니다."));
    }

    // Goal ID로 골 찾기
    public Goal isGoal(GoalRepository goalRepository, Long goalId) {
        return goalRepository.findById(goalId)
                            .orElseThrow(() -> new CustomException("Goal Id를 확인해주세요."));
    }

    // Post ID로 포스트 찾기
    public Post isPost(PostRepository postRepository, Long postId) {
        return postRepository.findById(postId)
                    .orElseThrow(() -> new CustomException("Post Id를 확인해주세요."));
    }

    // 나의 Comment 찾기
    public PostComment isMyComment(MemberRepository memberRepository, CommentRepository commentRepository, Long commentId) {
        Member member = isMemberCurrent(memberRepository);
        return commentRepository.findByIdAndMember(commentId, member)
                    .orElseThrow(() -> new CustomException("나의 Comment Id가 아닙니다."));
    }
}
