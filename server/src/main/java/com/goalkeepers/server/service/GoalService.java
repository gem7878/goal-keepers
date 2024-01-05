package com.goalkeepers.server.service;

import java.io.IOException;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.goalkeepers.server.config.SecurityUtil;
import com.goalkeepers.server.dto.GoalRequestDto;
import com.goalkeepers.server.dto.GoalResponseDto;
import com.goalkeepers.server.dto.GoalUpdateRequestDto;
import com.goalkeepers.server.entity.Goal;
import com.goalkeepers.server.entity.Member;
import com.goalkeepers.server.entity.Post;
import com.goalkeepers.server.exception.CustomException;
import com.goalkeepers.server.repository.GoalRepository;
import com.goalkeepers.server.repository.GoalShareRepository;
import com.goalkeepers.server.repository.MemberRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class GoalService extends CommonService{
    
    private final GoalRepository goalRepository;
    private final MemberRepository memberRepository;
    private final LikeShareService shareService;
    private final GoalShareRepository shareRepository;
    private final S3ImageFileService imageFileService;
    

    /*
     *  나의 전체 버킷리스트 보기
        버킷 상세 보기
        버킷 생성
        버킷 삭제
        버킷 수정
     */

    public Page<GoalResponseDto> getMyGoalList(int pageNumber) {
        Long memberId = SecurityUtil.getCurrentMemberId();
        return goalRepository.searchMyAllGoal(PageRequest.of(pageNumber - 1, 10), memberId);
    }

    // 모든 유저가 접근 가능
    public GoalResponseDto getSelectedGoal(Long goalId) {
        Long memberId = SecurityUtil.getCurrentMemberId();
        Member member = memberId != null ? memberRepository.findById(memberId).get() : null;
        Goal goal = goalRepository.findById(goalId)
                                    .orElseThrow(() -> new CustomException("Goal Id를 확인해주세요."));
        if(member == null) {
            return GoalResponseDto.of(goal);
        }
        Boolean isShare = shareRepository.existsByMemberAndGoal(member, goal);
        return GoalResponseDto.of(goal, isShare);
    }

    public GoalResponseDto createMyGoal(GoalRequestDto requestDto, String imageUrl) {
        Member member = isMemberCurrent(memberRepository);    
        return GoalResponseDto.of(goalRepository.save(requestDto.toGoal(member, imageUrl)));
    }

    public GoalResponseDto updateMyGoal(GoalUpdateRequestDto requestDto, Long goalId, MultipartFile multipartFile) throws IOException {
        Goal currentGoal = isMyGoal(memberRepository, goalRepository, goalId);
        
        if (multipartFile == null && requestDto != null) {
            return GoalResponseDto.of(Goal.goalUpdate(currentGoal, requestDto));
        } else {
            String imageUrl = currentGoal.getImageUrl();
            if (imageUrl != null) {
                // 원래 골의 이미지 삭제
                String[] urlArray = imageUrl.split("/");
                imageFileService.delete(urlArray[3], urlArray[4]);
            }            
            // 새로운 이미지 업로드
            String newImageUrl = imageFileService.upload(multipartFile, "images");
            // 업로드된 이미지 주소로 DB 업데이트
            if (requestDto == null) {
                return GoalResponseDto.of(Goal.goalUpdate(currentGoal, newImageUrl));
            }
            return GoalResponseDto.of(Goal.goalUpdate(currentGoal, requestDto, newImageUrl));
        }
    }

    public void deleteMyGoal(Long goalId) {
        Goal goal = isMyGoal(memberRepository, goalRepository, goalId);
        String imageUrl = goal.getImageUrl();
        if (imageUrl != null) {
            String[] urlArray = imageUrl.split("/");
            imageFileService.delete(urlArray[3], urlArray[4]);
        }
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
