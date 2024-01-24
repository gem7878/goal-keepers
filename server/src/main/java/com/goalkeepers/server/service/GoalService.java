package com.goalkeepers.server.service;

import java.util.List;
import java.util.Objects;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.io.IOException;

import org.springframework.context.annotation.DependsOn;
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
import com.goalkeepers.server.entity.PostContent;
import com.goalkeepers.server.exception.CustomException;
import com.goalkeepers.server.repository.GoalRepository;
import com.goalkeepers.server.repository.GoalShareRepository;
import com.goalkeepers.server.repository.MemberRepository;
import com.google.firebase.FirebaseException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
@DependsOn("firebaseStorageService")
public class GoalService extends CommonService{
    
    private final GoalRepository goalRepository;
    private final MemberRepository memberRepository;
    private final LikeShareService shareService;
    private final GoalShareRepository shareRepository;
    private final FirebaseStorageService firebaseStorageService;
    private final BoardService boardService;
    

    /*
     *  나의 전체 버킷리스트 보기
        버킷 상세 보기
        버킷 생성
        버킷 삭제
        버킷 수정
     */

    public Page<GoalResponseDto> getMyGoalList(int pageNumber) {
        Long memberId = SecurityUtil.getCurrentMemberId();
        return goalRepository.searchMyAllGoal(PageRequest.of(pageNumber - 1, 18), memberId);
    }

    // 모든 유저가 접근 가능
    public GoalResponseDto getSelectedGoal(Long goalId) {
        Long memberId = SecurityUtil.getCurrentMemberId();
        Member member = memberId != null ? memberRepository.findById(memberId).get() : null;
        Goal goal = goalRepository.findById(goalId)
                                    .orElseThrow(() -> new CustomException("Goal Id를 확인해주세요."));
        String imageUrl = goal.getImageUrl();
        if(Objects.nonNull(imageUrl) && !imageUrl.isEmpty()) {
            imageUrl = firebaseStorageService.showFile(imageUrl);
        }
        if(member == null) {
            return GoalResponseDto.of(goal, imageUrl, findJoinMemberList(goal));
        }
        Boolean isShare = shareRepository.existsByMemberAndGoal(member, goal);
        return GoalResponseDto.of(goal, imageUrl, isShare, findJoinMemberList(goal));
    }

    public Long createMyGoal(GoalRequestDto requestDto, String imageUrl) {
        Member member = isMemberCurrent(memberRepository);
        Goal goal = goalRepository.save(requestDto.toGoal(member, imageUrl));
        return goal.getId();
        //return GoalResponseDto.of(goalRepository.save(requestDto.toGoal(member, imageUrl)), null);
    }

    public void updateMyGoal(GoalUpdateRequestDto requestDto, Long goalId, MultipartFile multipartFile) throws IOException, FirebaseException {
        Goal currentGoal = isMyGoal(memberRepository, goalRepository, goalId);
        
        if (multipartFile == null && requestDto != null) {
            /* 이미지를 변경하지 않고 삭제 */
            if(requestDto.getDeleteImage() == true) {
                String imageUrl = currentGoal.getImageUrl();
                if (Objects.nonNull(imageUrl) && !imageUrl.isEmpty()) {
                    firebaseStorageService.deleteFile(imageUrl);
                }
                Goal.goalUpdate(currentGoal, requestDto, null);
                //return GoalResponseDto.of(Goal.goalUpdate(currentGoal, requestDto, null), findJoinMemberList(currentGoal));
            } else {
                /* 이미지 변경 안함 */
                Goal.goalUpdate(currentGoal, requestDto, null);
                //return GoalResponseDto.of(Goal.goalUpdate(currentGoal, requestDto, null), showImageUrl, findJoinMemberList(currentGoal));         
            }         
        } else {
            /* 이미지 변경 */
            String imageUrl = currentGoal.getImageUrl();
            if (Objects.nonNull(imageUrl) && !imageUrl.isEmpty()) {
                // 원래 골의 이미지 삭제
                firebaseStorageService.deleteFile(imageUrl);
            }
            // 새로운 이미지 업로드
            String newImageUrl = firebaseStorageService.upload(multipartFile, "images");
            
            // 이미지 url 가져오기
            //String showImageUrl = firebaseStorageService.showFile(newImageUrl);

            // DB 업데이트
            Goal.goalUpdate(currentGoal, requestDto, newImageUrl);
            //return GoalResponseDto.of(Goal.goalUpdate(currentGoal, requestDto, newImageUrl), showImageUrl, findJoinMemberList(currentGoal));
        }
    }

    public String deleteMyGoal(Long goalId) {
        Goal goal = isMyGoal(memberRepository, goalRepository, goalId);
        Member member = isMemberCurrent(memberRepository);
        String imageUrl = goal.getImageUrl();
        if (Objects.nonNull(imageUrl) && !imageUrl.isEmpty()) {
            firebaseStorageService.deleteFile(imageUrl);
        }
        // 포스트 지우기
        List<PostContent> contents = boardService.getMyPostContentWithGoal(goal);
        for (PostContent content : contents) {
            boardService.deleteMyPostContent(content.getId());
        }
        if(goal.getShareCnt() == 0 && !shareRepository.existsByMemberAndGoal(member, goal)) {
            if(Objects.isNull(goal.getShare())) {
                // 쉐어 지우기
                shareService.deleteShare(goal);
            }
            goalRepository.deleteById(goalId);
            return "삭제";
        } else {
            // 참여한 사람들이 있을 때
            // title, share_cnt, image_url 제외 정보 지우기
            Goal.disconnectedGoal(goal);
            return "정보 삭제";
        }
    }

    public String completeMyGoal(Long goalId) {
        Goal goal = isMyGoal(memberRepository, goalRepository, goalId);
        if(goal.isCompleted()) {
            goal.setCompleted(false);
            goal.setCompleteDate(null);
            return goalId + " Goal 완료 취소하였습니다.";
        } else {
            LocalDateTime today = LocalDateTime.now();
            goal.setCompleted(true);
            goal.setCompleteDate(today);
            return goalId + " Goal 완료하였습니다.";
        }
        
    }
}
