package com.goalkeepers.server.common;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.Object;
import java.util.Optional;
import java.util.List;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Map;
import java.util.HashMap;
import java.util.Set;

import com.goalkeepers.server.config.SecurityUtil;
import com.goalkeepers.server.entity.Goal;
import com.goalkeepers.server.entity.GoalShare;
import com.goalkeepers.server.entity.Member;
import com.goalkeepers.server.entity.Post;
import com.goalkeepers.server.entity.PostComment;
import com.goalkeepers.server.entity.PostContent;
import com.goalkeepers.server.entity.Setting;
import com.goalkeepers.server.entity.TYPE;
import com.goalkeepers.server.exception.CustomException;
import com.goalkeepers.server.exception.ErrorCode;
import com.goalkeepers.server.repository.CommentRepository;
import com.goalkeepers.server.repository.GoalRepository;
import com.goalkeepers.server.repository.GoalShareRepository;
import com.goalkeepers.server.repository.MemberRepository;
import com.goalkeepers.server.repository.PostContentRepository;
import com.goalkeepers.server.repository.PostRepository;
import com.goalkeepers.server.repository.SettingRepository;


@Transactional
@Service
public class ServiceHelper {
    
    // 로그인 했는지 확인
    public Member isMemberCurrent(MemberRepository memberRepository) {
        return memberRepository.findById(SecurityUtil.getCurrentMemberId())
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));
    }

    // 나의 Goal 찾기
    public Goal isMyGoal(MemberRepository memberRepository, GoalRepository goalRepository, Long goalId) {
        Member member = isMemberCurrent(memberRepository);
        return goalRepository.findByIdAndMember(goalId, member)
                            .orElseThrow(() -> new CustomException(ErrorCode.BAD_REQUEST, "나의 Goal Id가 아닙니다."));
    }

    // 나의 PostContent 찾기
    public PostContent isMyPostContent(MemberRepository memberRepository, PostContentRepository contentRepository, Long contentId) {
        Member member = isMemberCurrent(memberRepository);
        return contentRepository.findByIdAndMember(contentId, member)
                            .orElseThrow(() -> new CustomException(ErrorCode.BAD_REQUEST, "나의 PostContent Id가 아닙니다."));
    }

    // Goal ID로 골 찾기
    public Goal isGoal(GoalRepository goalRepository, Long goalId) {
        return goalRepository.findById(goalId)
                            .orElseThrow(() -> new CustomException(ErrorCode.BAD_REQUEST, "Goal Id를 확인해주세요."));
    }

    // Post ID로 포스트 찾기
    public Post isPost(PostRepository postRepository, Long postId) {
        return postRepository.findById(postId)
                    .orElseThrow(() -> new CustomException(ErrorCode.BAD_REQUEST, "Post Id를 확인해주세요."));
    }

    // PostContent ID로 content 찾기
    public PostContent isPostContent(PostContentRepository contentRepository, Long contentId) {
        return contentRepository.findById(contentId)
                            .orElseThrow(() -> new CustomException(ErrorCode.BAD_REQUEST, "PostContent Id를 확인해주세요."));
    }

    // 나의 Comment 찾기
    public PostComment isMyComment(MemberRepository memberRepository, CommentRepository commentRepository, Long commentId) {
        Member member = isMemberCurrent(memberRepository);
        return commentRepository.findByIdAndMember(commentId, member)
                    .orElseThrow(() -> new CustomException(ErrorCode.BAD_REQUEST, "나의 Comment Id가 아닙니다."));
    }

    // joinMemberList 가져오기
    public List<Map<String, Object>> findJoinMemberList(Goal currentGoal, GoalShareRepository shareRepository) {
        // currentGoal
        Goal goal = currentGoal;
        // shareGoal로 바꾸기
        GoalShare share = goal.getShare();
        if(Objects.nonNull(share)) {
            goal = Optional.ofNullable(share.getGoal()).orElse(null);
        }
        // shareGoal이 삭제됐을 때
        if(Objects.isNull(goal)) {
            return null;
        }
        List<Map<String, Object>> joinMemberList = new ArrayList<>();
        if(Objects.nonNull(goal) && Objects.nonNull(goal.getMember())) {
            Map<String, Object> owner = new HashMap<>();
            owner.put("memberId", goal.getMember().getId());
            owner.put("nickname", goal.getMember().getNickname());
            owner.put("isOwner", true);
            joinMemberList.add(owner);
        }
        Set<GoalShare> shares = shareRepository.findAllByGoal(goal);
        for(GoalShare goalShare : shares) {
            Map<String, Object> joinMember = new HashMap<>();
            joinMember.put("memberId", goalShare.getMember().getId());
            joinMember.put("nickname", goalShare.getMember().getNickname());
            joinMember.put("isOwner", false);
            joinMemberList.add(joinMember);
        }
        return joinMemberList;
    }

    // 각 알림 허용 유저인지
    public Member alarmTrueReceiver(SettingRepository settingRepository, Member member, TYPE type) {
        if (Objects.isNull(member)) {
            return null;
        }
        Setting setting = settingRepository.findByMember(member).orElseThrow(() -> new CustomException(ErrorCode.RESOURCE_NOT_FOUND, member + "의 setting 데이터를 찾지 못하였습니다."));
        boolean alarmIsTrue = false;
        switch (type) {
            case SHARE:
                alarmIsTrue = setting.isGoalShareAlarm();
                break;
            case LIKE:
                alarmIsTrue = setting.isContentLikeAlarm();
                break;
            case COMMENT:
                alarmIsTrue = setting.isCommentAlarm();
                break;
            case CHEER:
                alarmIsTrue = setting.isPostCheerAlarm();
                break;
            case TODAY:
                alarmIsTrue = setting.isTodayAlarm();
                break;
            case WEEKLEFT:
            case DAYLEFT:
            case DDAY:
                alarmIsTrue = setting.isDdayAlarm();
                break;
            default:
                break;
        }
        
        if(alarmIsTrue) {
            return member;
        } else {
            return null;
        }
    }

    // 존재하는 이메일인지
    public boolean isExistsEmail(MemberRepository memberRepository, String email) {
        if(memberRepository.existsByEmail(email)) {
            throw new CustomException(ErrorCode.DUPLICATE_RESOURCE, "이미 가입된 이메일입니다.");
        } else {
            return true;
        }
    } 

    // 존재하는 닉네임인지
    public boolean isExistsNickname(MemberRepository memberRepository, String nickname) {
        if(memberRepository.existsByNickname(nickname)) {
            throw new CustomException(ErrorCode.DUPLICATE_RESOURCE, "사용중인 닉네임입니다.");
        } else {
            return true;
        }
    } 
}
