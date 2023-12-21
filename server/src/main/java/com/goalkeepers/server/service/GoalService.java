package com.goalkeepers.server.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.goalkeepers.server.config.SecurityUtil;
import com.goalkeepers.server.dto.GoalRequestDto;
import com.goalkeepers.server.dto.GoalResponseDto;
import com.goalkeepers.server.dto.PostResponseDto;
import com.goalkeepers.server.entity.Goal;
import com.goalkeepers.server.entity.Member;
import com.goalkeepers.server.repository.GoalRepository;
import com.goalkeepers.server.repository.MemberRepository;
import com.goalkeepers.server.repository.PostRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class GoalService {
    
    private final GoalRepository goalRepository;
    private final MemberRepository memberRepository;
    private final PostRepository postRepository;

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

    public List<PostResponseDto> getSelectedGoal(Long goalId) {
        Member member = isMemberCurrent();
        Optional<Goal> goal = goalRepository.findById(goalId);

        if (goal.isPresent()) {
            return postRepository.findAllByGoal(goal)
                    .stream()
                    .map(PostResponseDto::listOf)
                    .collect(Collectors.toList());
        } else {
            throw new RuntimeException("GoalId를 확인해주세요.");
        }
    }

    public GoalResponseDto createMyGoal(GoalRequestDto requestDto) {
        Member member = isMemberCurrent();
        Goal goal = requestDto.toGoal(member);     
        return GoalResponseDto.of(goalRepository.save(goal));
    }

    public GoalResponseDto updateMyGoal(GoalRequestDto requestDto, Long goalId) {
        Member member = isMemberCurrent();
        Goal goal = goalRepository.findById(goalId)
                    .orElseThrow(() -> new RuntimeException("Goal Id를 확인해주세요."));
        if (goal.getMember().equals(member)) {
            return GoalResponseDto.of(Goal.goalUpdate(goal, requestDto));
        } else {
            throw new RuntimeException("로그인한 유저와 작성 유저가 같지 않습니다.");
        }
    }

    public void deleteMyGoal(Long goalId) {
        Member member = isMemberCurrent();
        Goal goal = goalRepository.findById(goalId)
                    .orElseThrow(() -> new RuntimeException("Goal Id를 확인해주세요."));
        if (goal.getMember().equals(member)) {
            goalRepository.delete(goal);
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
