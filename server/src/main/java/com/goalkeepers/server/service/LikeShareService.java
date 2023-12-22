package com.goalkeepers.server.service;

import java.time.LocalDate;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.goalkeepers.server.config.SecurityUtil;
import com.goalkeepers.server.dto.GoalResponseDto;
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

    // 연결된 골 찾기
    public GoalResponseDto findGoal(GoalShareRequestDto requestDto) {
        Member member = isMemberCurrent();
        Goal goal = goalRepository.findById(requestDto.getGoalId())
                    .orElseThrow(() -> new RuntimeException("Goal Id를 확인해주세요."));
        GoalShare share = shareRepository.findByMemberAndGoal(member, goal)
                                        .orElseThrow(() -> new RuntimeException("이 Goal과 연결된 나의 Goal이 없습니다."));
        
        Goal shareGoal = goalRepository.findByShare(share)
                                        .orElseThrow(() -> new RuntimeException("나의 Goal이 없습니다."));
        return GoalResponseDto.of(shareGoal);
    }

    // 공유하기 -> 골 만들기
    public GoalResponseDto addShare(GoalShareRequestDto requestDto) {
        Member member = isMemberCurrent();
        Goal goal = goalRepository.findById(requestDto.getGoalId())
                    .orElseThrow(() -> new RuntimeException("Goal Id를 확인해주세요."));

        // 공유한 적이 없는지 -> 내 골이 아닌지
        if (shareRepository.existsByMemberAndGoal(member, goal)) {
            throw new RuntimeException("공유된 Goal입니다.");
        } else if (goal.getMember().equals(member)) {
            throw new RuntimeException("내 Goal입니다.");
        } else {
            GoalShare share = shareRepository.save(new GoalShare(member, goal));
            goal.setShareCnt(goal.getShareCnt()+1);
            
            // 새로운 골 만들기
            LocalDate startDate = LocalDate.now();
            Goal newGoal = goalRepository.save(new Goal(
                                share,
                                goal.getTitle(),
                                goal.getDescription(),
                                goal.getImageUrl(),
                                startDate,
                                startDate.plusYears(1),
                                member));
            return GoalResponseDto.of(newGoal);
        }
    }

    // Share 데이터 삭제
    public void deleteShare(Goal goal) {
        GoalShare share = goal.getShare();
        if (share != null) {
            Goal sharedGoal = share.getGoal();
            sharedGoal.setShareCnt(sharedGoal.getShareCnt()-1);
            goal.setShare(null);
            shareRepository.delete(share);
        }
    }


    // 로그인 했는지 확인
    public Member isMemberCurrent() {
        return memberRepository.findById(SecurityUtil.getCurrentMemberId())
                .orElseThrow(() -> new RuntimeException("로그인 유저 정보가 없습니다"));
    }
}
