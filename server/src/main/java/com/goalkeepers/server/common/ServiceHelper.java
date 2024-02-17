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
import com.goalkeepers.server.repository.CommentRepository;
import com.goalkeepers.server.repository.GoalRepository;
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
                .orElseThrow(() -> new CustomException("로그인 유저 정보가 없습니다."));
    }

    // 나의 Goal 찾기
    public Goal isMyGoal(MemberRepository memberRepository, GoalRepository goalRepository, Long goalId) {
        Member member = isMemberCurrent(memberRepository);
        return goalRepository.findByIdAndMember(goalId, member)
                            .orElseThrow(() -> new CustomException("나의 Goal Id가 아닙니다."));
    }

    // 나의 PostContent 찾기
    public PostContent isMyPostContent(MemberRepository memberRepository, PostContentRepository contentRepository, Long contentId) {
        Member member = isMemberCurrent(memberRepository);
        return contentRepository.findByIdAndMember(contentId, member)
                            .orElseThrow(() -> new CustomException("나의 PostContent Id가 아닙니다."));
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

    // PostContent ID로 content 찾기
    public PostContent isPostContent(PostContentRepository contentRepository, Long contentId) {
        return contentRepository.findById(contentId)
                            .orElseThrow(() -> new CustomException("PostContent Id를 확인해주세요."));
    }

    // 나의 Comment 찾기
    public PostComment isMyComment(MemberRepository memberRepository, CommentRepository commentRepository, Long commentId) {
        Member member = isMemberCurrent(memberRepository);
        return commentRepository.findByIdAndMember(commentId, member)
                    .orElseThrow(() -> new CustomException("나의 Comment Id가 아닙니다."));
    }

    public List<Map<String, Object>> findJoinMemberList(Goal currentGoal) {
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
        for(GoalShare goalShare : goal.getShareList()) {
            Map<String, Object> member = new HashMap<>();
            member.put("memberId", goalShare.getMember().getId());
            member.put("nickname", goalShare.getMember().getNickname());
            joinMemberList.add(member);
        }
        return joinMemberList;
    }
    public Member alarmTrueReceiver(SettingRepository settingRepository, Member member, TYPE type) {
        if (Objects.isNull(member)) {
            return null;
        }
        Setting setting = settingRepository.findByMember(member).orElseThrow(() -> new CustomException(member + "의 setting 데이터를 찾지 못하였습니다."));
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
}
