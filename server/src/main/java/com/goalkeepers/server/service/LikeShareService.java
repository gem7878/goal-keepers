package com.goalkeepers.server.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.goalkeepers.server.config.SecurityUtil;
import com.goalkeepers.server.dto.GoalShareRequestDto;
import com.goalkeepers.server.dto.PostLikeRequestDto;
import com.goalkeepers.server.entity.Goal;
import com.goalkeepers.server.entity.GoalShare;
import com.goalkeepers.server.entity.Member;
import com.goalkeepers.server.entity.Post;
import com.goalkeepers.server.entity.PostLike;
import com.goalkeepers.server.repository.GoalRepository;
import com.goalkeepers.server.repository.GoalShareRepository;
import com.goalkeepers.server.repository.MemberRepository;
import com.goalkeepers.server.repository.PostLikeRepository;
import com.goalkeepers.server.repository.PostRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class LikeShareService {
    
    private final PostLikeRepository likeRepository;
    private final GoalShareRepository shareRepository;
    private final MemberRepository memberRepository;
    private final GoalRepository goalRepository;
    private final PostRepository postRepository;


    // 좋아요
    public void addLike(PostLikeRequestDto requestDto) {
        Member member = isMemberCurrent();
        Post post = postRepository.findById(requestDto.getPostId())
                    .orElseThrow(() -> new RuntimeException("Post Id를 확인해주세요."));
        
        if(likeRepository.existsByMemberAndPost(member, post)) {
            // 좋아요 취소
            post.setLikeCnt(post.getLikeCnt()-1);
            likeRepository.deleteByMemberAndPost(member, post);
            throw new RuntimeException("좋아요 취소");
        } else {
            // 좋아요
            post.setLikeCnt(post.getLikeCnt()+1);
            likeRepository.save(new PostLike(member, post));
        }
    }
    

    // 공유하기
    public void addShare(GoalShareRequestDto requestDto) {
        Member member = isMemberCurrent();
        Goal goal = goalRepository.findById(requestDto.getGoalId())
                    .orElseThrow(() -> new RuntimeException("Goal Id를 확인해주세요."));

        if(shareRepository.existsByMemberAndGoal(member, goal)) {
            // 공유 취소
            goal.setShareCnt(goal.getShareCnt()-1);
            shareRepository.deleteByMemberAndGoal(member, goal);
            throw new RuntimeException("공유 취소");
        } else {
            // 공유
            goal.setShareCnt(goal.getShareCnt()+1);
            shareRepository.save(new GoalShare(member, goal));
        }
    }


    // 로그인 했는지 확인
    public Member isMemberCurrent() {
        return memberRepository.findById(SecurityUtil.getCurrentMemberId())
                .orElseThrow(() -> new RuntimeException("로그인 유저 정보가 없습니다"));
    }
}
