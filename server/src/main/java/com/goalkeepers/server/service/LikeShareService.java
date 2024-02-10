package com.goalkeepers.server.service;

import java.time.LocalDate;
import java.util.Objects;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.goalkeepers.server.dto.GoalResponseDto;
import com.goalkeepers.server.dto.GoalShareRequestDto;
import com.goalkeepers.server.dto.PostCheerRequestDto;
import com.goalkeepers.server.dto.ContentLikeRequestDto;
import com.goalkeepers.server.entity.Goal;
import com.goalkeepers.server.entity.GoalShare;
import com.goalkeepers.server.entity.Member;
import com.goalkeepers.server.entity.Post;
import com.goalkeepers.server.entity.PostCheer;
import com.goalkeepers.server.entity.PostContent;
import com.goalkeepers.server.entity.PostLike;
import com.goalkeepers.server.entity.TYPE;
import com.goalkeepers.server.exception.CustomException;
import com.goalkeepers.server.repository.GoalRepository;
import com.goalkeepers.server.repository.GoalShareRepository;
import com.goalkeepers.server.repository.MemberRepository;
import com.goalkeepers.server.repository.PostCheerRepository;
import com.goalkeepers.server.repository.PostContentRepository;
import com.goalkeepers.server.repository.PostLikeRepository;
import com.goalkeepers.server.repository.PostRepository;
import com.goalkeepers.server.repository.SettingRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class LikeShareService extends CommonService {
    
    private final PostLikeRepository likeRepository;
    private final GoalShareRepository shareRepository;
    private final MemberRepository memberRepository;
    private final GoalRepository goalRepository;
    private final PostContentRepository contentRepository;
    private final PostRepository postRepository;
    private final PostCheerRepository cheerRepository;
    private final SettingRepository settingRepository;
    private final FirebaseStorageService firebaseStorageService;
    private final NotificationService notificationService;

    // 컨텐트 좋아요
    public String addLike(ContentLikeRequestDto requestDto) {
        Member member = isMemberCurrent(memberRepository);
        PostContent content = isPostContent(contentRepository, requestDto.getContentId());
        
        if(likeRepository.existsByMemberAndPostContent(member, content)) {
            // 좋아요 취소
            content.setLikeCnt(content.getLikeCnt() - 1);
            likeRepository.deleteByMemberAndPostContent(member, content);
            return " 좋아요 취소";
        } else {
            // 좋아요
            content.setLikeCnt(content.getLikeCnt() + 1);
            likeRepository.save(new PostLike(member, content));

            // 알림 보내기
            Member receiver = alarmTrueReceiver(settingRepository, member, TYPE.LIKE);
            if(Objects.nonNull(receiver) && !member.equals(receiver)) {
                Post post = content.getPost();
                notificationService.send(receiver, member, TYPE.LIKE, post.getId(), post.getGoal().getTitle(), null, null);
            }
            return " 좋아요";
        }
    }

    // 포스트 응원해요
    public String addPostCheer(PostCheerRequestDto requestDto) {
        Member member = isMemberCurrent(memberRepository);
        Post post = isPost(postRepository, requestDto.getPostId());
        PostCheer cheer = cheerRepository.findByMemberAndPost(member, post).orElse(null);
        if(Objects.nonNull(cheer)) {
            // 응원해요 취소
            post.setCheerCnt(post.getCheerCnt() - 1);
            cheerRepository.delete(cheer);
            return " 응원해요 취소";
        } else {
            // 응원해요
            post.setCheerCnt(post.getCheerCnt() + 1);
            cheerRepository.save(new PostCheer(member, post));

            // 알림 보내기
            Member receiver = alarmTrueReceiver(settingRepository, member, TYPE.CHEER);
            if(Objects.nonNull(receiver) && !member.equals(receiver)) {
                notificationService.send(receiver, member, TYPE.CHEER, post.getId(), post.getGoal().getTitle(), null, null);
            }
            return " 응원해요";
        }
    }

    // 연결된 골 찾기
    public GoalResponseDto findGoal(Long goalId) {
        Member member = isMemberCurrent(memberRepository);
        Goal goal = isGoal(goalRepository, goalId);
        if(Objects.nonNull(goal.getMember()) && goal.getMember().equals(member)) {
            throw new CustomException("나의 Goal입니다.");
        }
        GoalShare share = shareRepository.findByMemberAndGoal(member, goal)
                        .orElseThrow(() -> new CustomException("이 Goal과 연결된 나의 Goal이 없습니다."));
        Goal myGoal = goalRepository.findByShare(share).orElseThrow(() -> new CustomException("이 Goal과 연결된 나의 Goal이 존재하나 데이터베이스 상에 오류가 있습니다."));
        return GoalResponseDto.of(myGoal, null, null);
    }

    // 공유하기 -> 골 만들기
    public void addShare(GoalShareRequestDto requestDto) {
        Member member = isMemberCurrent(memberRepository);
        
        Goal goal = isGoal(goalRepository, requestDto.getGoalId());
        GoalShare goalShare = goal.getShare();
        // goal이 shareGoal일 경우엔 shareGoal 정보로 바꾸기
        if(Objects.nonNull(goalShare)) {
            goal = goalShare.getGoal();
        }

        // 공유한 적이 없는지 -> 내 골이 아닌지 -> 골 만들기
        if (shareRepository.existsByMemberAndGoal(member, goal)) {
            throw new CustomException("담기한 Goal입니다.");
        } else if (Objects.nonNull(goal.getMember()) && goal.getMember().equals(member)) {
            throw new CustomException("나의 Goal입니다.");
        } else {
            GoalShare share = shareRepository.save(new GoalShare(member, goal));
            goal.setShareCnt(goal.getShareCnt()+1);
            
            // 새로운 골 만들기
            String originImageUrl = goal.getImageUrl();
            String copyImageName = originImageUrl;
            if(originImageUrl != null) {
                copyImageName = firebaseStorageService.copyAndRenameFile(originImageUrl, "images");
            }
            LocalDate startDate = LocalDate.now();
            goalRepository.save(new Goal(
                                share,
                                goal.getTitle(),
                                goal.getDescription(),
                                copyImageName,
                                startDate,
                                startDate.plusYears(1),
                                member));

            // 알림 보내기
            Member receiver = alarmTrueReceiver(settingRepository, member, TYPE.SHARE);
            if(Objects.nonNull(receiver) && !member.equals(receiver)) {
                notificationService.send(receiver, member, TYPE.SHARE, goal.getId(), goal.getTitle(), null, null);
            }            
            //return GoalResponseDto.of(newGoal, null);
        }
    }

    // 공유 취소 - 참여 제외
    public void disconnecteOriginGoal(Long goalId) {
        Goal goal = isMyGoal(memberRepository, goalRepository, goalId);
        deleteShare(goal);
    }

    // Goal Delete - Share 데이터 삭제
    public void deleteShare(Goal goal) {
        GoalShare share = goal.getShare();
        if (Objects.nonNull(share)) {
            Goal sharedGoal = share.getGoal();
            sharedGoal.setShareCnt(sharedGoal.getShareCnt()-1);
            goal.setShare(null);
            shareRepository.delete(share);
        }
    }

    // PostContent Delete - Like 데이터 삭제*
    public void deleteLike(PostContent content) {
        List<PostLike> likeList = likeRepository.findAllByPostContent(content);
        for (PostLike like : likeList) {
            likeRepository.delete(like);
        }
    }

}
