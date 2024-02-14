package com.goalkeepers.server.common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import com.goalkeepers.server.config.SecurityUtil;
import com.goalkeepers.server.entity.Goal;
import com.goalkeepers.server.entity.GoalShare;
import com.goalkeepers.server.entity.Member;
import com.goalkeepers.server.entity.Post;
import com.goalkeepers.server.entity.PostContent;
import com.goalkeepers.server.repository.GoalShareRepository;
import com.goalkeepers.server.repository.MemberRepository;
import com.goalkeepers.server.repository.PostCheerRepository;
import com.goalkeepers.server.repository.PostLikeRepository;
import com.goalkeepers.server.service.FirebaseStorageService;

public class CommonUtils {

    // 이미지 URL
    public static String getImageUrl(Goal goal, FirebaseStorageService firebaseStorageService) {
        String imageUrl = null;
        if(Objects.nonNull(goal) && Objects.nonNull(goal.getImageUrl())) {
            imageUrl = firebaseStorageService.showFile(goal.getImageUrl());
        }
        return imageUrl;
    }

    // 담기했는지
    public static boolean isShareGoal(Goal goal, Member member, GoalShareRepository shareRepository) {
        boolean isShare = false;
        if(Objects.nonNull(goal.getShare())) {
            goal = goal.getShare().getGoal();
        }
        if(Objects.nonNull(member)) {
            isShare = shareRepository.existsByMemberAndGoal(member, goal);
        }
        return isShare;
    }

    // 좋아요했는지
    public static boolean isLikeContent(PostContent content, Member member, PostLikeRepository likeRepository) {
        boolean isLike = false;
        if(Objects.nonNull(member)) {
            isLike = likeRepository.existsByMemberAndPostContent(member, content);
        }
        return isLike;
    }

    // 로그인이 안 되어있으면 null
    public static Member MemberOrNull(MemberRepository memberRepository) {
        return memberRepository.findById(SecurityUtil.getCurrentMemberId()).orElse(null);
    }
    
    // joinMemberList 가져오기
    public static List<Map<String, Object>> getJoinMemberList(Goal goal) {
        List<Map<String, Object>> joinMemberList = new ArrayList<>();
        if(Objects.nonNull(goal) && Objects.nonNull(goal.getShareList())) {
            for(GoalShare goalShare : goal.getShareList()) {
                Map<String, Object> joinMember = new HashMap<>();
                joinMember.put("memberId", goalShare.getMember().getId());
                joinMember.put("nickname", goalShare.getMember().getNickname());
                joinMemberList.add(joinMember);
            }
        }
        return joinMemberList;
    }

    // 응원해요 했는지
    public static boolean isCheerPost(Post post, Member member, PostCheerRepository cheerRepository) {
        boolean isCheer = false;
        if(Objects.nonNull(member)) {
            isCheer = cheerRepository.existsByMemberAndPost(member, post);
        }
        return isCheer;
    }

    public static int getOriginalGoalShareCnt(Goal goal) {
        return Objects.nonNull(goal.getShare()) ? goal.getShare().getGoal().getShareCnt() : goal.getShareCnt();
    }

    public static Member getWriter(PostContent content) {
        return content.getMember();
    }
}
