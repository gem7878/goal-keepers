package com.goalkeepers.server.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.goalkeepers.server.config.SecurityUtil;
import com.goalkeepers.server.dto.GoalPostResponseDto;
import com.goalkeepers.server.dto.GoalRequestDto;
import com.goalkeepers.server.dto.GoalResponseDto;
import com.goalkeepers.server.entity.Goal;
import com.goalkeepers.server.entity.Member;
import com.goalkeepers.server.entity.Post;
import com.goalkeepers.server.exception.CustomException;
import com.goalkeepers.server.repository.GoalRepository;
import com.goalkeepers.server.repository.MemberRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class GoalService {
    
    private final GoalRepository goalRepository;
    private final MemberRepository memberRepository;
    private final LikeShareService shareService;

    /*
     *  나의 전체 버킷리스트 보기
        버킷 상세 보기
        버킷 생성
        버킷 삭제
        버킷 수정
     */

    public List<GoalResponseDto> getMyGoalList() {
        Member member = isMemberCurrent();
        return goalRepository.findAllByMember(member)
                                .stream()
                                .map(GoalResponseDto::of)
                                .collect(Collectors.toList());
    }

    public List<GoalPostResponseDto> getSelectedGoal(Long goalId) {
        isMemberCurrent();
        Optional<Goal> goal = goalRepository.findById(goalId);

        if (goal.isPresent()) {
            return goal.get().getPosts()
                    .stream()
                    .map(GoalPostResponseDto::of)
                    .collect(Collectors.toList());
        } else {
            throw new CustomException("Goal Id를 확인해주세요.");
        }
    }

    public GoalResponseDto createMyGoal(GoalRequestDto requestDto) {
        Member member = isMemberCurrent();
        Goal goal = requestDto.toGoal(member);     
        return GoalResponseDto.of(goalRepository.save(goal));
    }

    public GoalResponseDto updateMyGoal(GoalRequestDto requestDto, Long goalId) {
        Member member = isMemberCurrent();
        Goal goal = goalRepository.findByIdAndMember(goalId, member)
                    .orElseThrow(() -> new CustomException("나의 Goal Id가 아닙니다."));
        return GoalResponseDto.of(Goal.goalUpdate(goal, requestDto));
    }

    public void deleteMyGoal(Long goalId) {
        Member member = isMemberCurrent();
        Goal goal = goalRepository.findByIdAndMember(goalId, member)
                    .orElseThrow(() -> new CustomException("나의 Goal Id가 아닙니다."));
        disconnectedPost(goal);
        shareService.deleteShare(goal);
        goalRepository.delete(goal);
    }

    private void disconnectedPost(Goal goal) {
        for (Post post : goal.getPosts()) {
            if(post.getGoal() != null) {
                post.setGoal(null);
            }
        }
    }

    // 로그인 했는지 확인
    public Member isMemberCurrent() {
        return memberRepository.findById(SecurityUtil.getCurrentMemberId())
                .orElseThrow(() -> new CustomException("로그인 유저 정보가 없습니다"));
    }
}
