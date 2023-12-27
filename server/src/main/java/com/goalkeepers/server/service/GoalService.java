package com.goalkeepers.server.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
public class GoalService extends CommonService{
    
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
        Member member = isMemberCurrent(memberRepository);
        return goalRepository.findAllByMember(member)
                                .stream()
                                .map(GoalResponseDto::of)
                                .collect(Collectors.toList());
    }

    // 모든 유저가 접근 가능
    public List<GoalPostResponseDto> getSelectedGoal(Long goalId) {
        isMemberCurrent(memberRepository);
        Goal goal = goalRepository.findById(goalId)
                                    .orElseThrow(() -> new CustomException("Goal Id를 확인해주세요."));

        return goal.getPosts()
                .stream()
                .map(GoalPostResponseDto::of)
                .collect(Collectors.toList());
    }

    public GoalResponseDto createMyGoal(GoalRequestDto requestDto) {
        Member member = isMemberCurrent(memberRepository);    
        return GoalResponseDto.of(goalRepository.save(requestDto.toGoal(member)));
    }

    public GoalResponseDto updateMyGoal(GoalRequestDto requestDto, Long goalId) {
        return GoalResponseDto.of(Goal.goalUpdate(isMyGoal(memberRepository, goalRepository, goalId), requestDto));
    }

    public void deleteMyGoal(Long goalId) {
        Goal goal = isMyGoal(memberRepository, goalRepository, goalId);

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
}
