package com.goalkeepers.server.service;

import java.util.List;
import java.util.Objects;
import java.util.Set;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.goalkeepers.server.common.ServiceHelper;
import com.goalkeepers.server.config.SecurityUtil;
import com.goalkeepers.server.dto.LoginRequestDto;
import com.goalkeepers.server.dto.MemberResponseDto;
import com.goalkeepers.server.dto.TokenDto;
import com.goalkeepers.server.entity.Goal;
import com.goalkeepers.server.entity.GoalShare;
import com.goalkeepers.server.entity.Member;
import com.goalkeepers.server.entity.Post;
import com.goalkeepers.server.entity.PostCheer;
import com.goalkeepers.server.entity.PostContent;
import com.goalkeepers.server.entity.PostLike;
import com.goalkeepers.server.exception.CustomException;
import com.goalkeepers.server.exception.ErrorCode;
import com.goalkeepers.server.repository.GoalRepository;
import com.goalkeepers.server.repository.GoalShareRepository;
import com.goalkeepers.server.repository.MemberRepository;
import com.goalkeepers.server.repository.PostCheerRepository;
import com.goalkeepers.server.repository.PostLikeRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MemberService extends ServiceHelper {
    
    private final MemberRepository memberRepository;
    private final GoalRepository goalRepository;
    private final GoalShareRepository shareRepository;
    private final PostLikeRepository likeRepository;
    private final PostCheerRepository cheerRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthService authService;
    private final GoalService goalService;

    @Transactional(readOnly = true)
    public MemberResponseDto getMyInfoBySecurity() {
        return memberRepository.findById(SecurityUtil.getCurrentMemberId())
                .map(MemberResponseDto::of)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));
    }

    // 닉네임 변경
    @Transactional
    public void changeMemberNickname(String nickname) {
        Member member = isMemberCurrent(memberRepository);
        member.setNickname(nickname);
        memberRepository.save(member);
    }

    // 비밀번호 변경
    @Transactional
    public TokenDto changeMemberPassword(String email, String exPassword, String newPassword) {
        Member member = isMemberCurrent(memberRepository);

        if(!member.getEmail().equals(email)) {
            throw new CustomException(ErrorCode.BAD_REQUEST, "로그인한 유저와 입력된 이메일이 다릅니다.");
        }

        if (!passwordEncoder.matches(exPassword, member.getPassword())) {
            throw new CustomException(ErrorCode.BAD_REQUEST, "원래의 비밀번호를 확인해주세요.");
        }

        if(exPassword.equals(newPassword)) {
            throw new CustomException(ErrorCode.BAD_REQUEST, "이전의 비밀번호와 변경하려는 비밀번호가 같습니다.");
        }

        member.setPassword(passwordEncoder.encode(newPassword));
        Member savedMember = memberRepository.save(member);
        LoginRequestDto requestDto = new LoginRequestDto(savedMember.getEmail(), newPassword);
		return authService.login(requestDto);
    }

    // 탈퇴 데이터 정리
    @Transactional
    public void deleteData(String email, String password) {
        Member member = isMemberCurrent(memberRepository);

        if(!member.getEmail().equals(email)) {
            throw new CustomException(ErrorCode.BAD_REQUEST, "이메일을 확인해주세요.");
        }

        if (!passwordEncoder.matches(password, member.getPassword())) {
            throw new CustomException(ErrorCode.BAD_REQUEST, "비밀번호를 확인해주세요.");
        }

        // 쉐어 카운트 -1
        Set<GoalShare> shares = shareRepository.findAllByMember(member);
        if (!shares.isEmpty()) {
            for (GoalShare share : shares) {
                Goal sharedGoal = share.getGoal();
                if(Objects.nonNull(sharedGoal)) {
                    sharedGoal.setShareCnt(sharedGoal.getShareCnt() - 1);
                }
                Goal goal = goalRepository.findByShare(share).orElse(null);
                if(Objects.nonNull(goal)) {
                    goal.setShare(null);
                }
                shareRepository.delete(share);
            }
        }

        // 라이크 카운트 -1
        List<PostLike> likes = likeRepository.findAllByMember(member);
        if(!likes.isEmpty()) {
            for (PostLike like : likes) {
                PostContent content = like.getPostContent();
                if(Objects.nonNull(content)) {
                    content.setLikeCnt(content.getLikeCnt() - 1);
                }
                likeRepository.delete(like);
            }
        }

        // 응원해요 카운트 -1
        List<PostCheer> cheers = cheerRepository.findAllByMember(member);
        if(!cheers.isEmpty()) {
            for (PostCheer cheer : cheers) {
                Post post = cheer.getPost();
                if(Objects.nonNull(post)) {
                    post.setCheerCnt(post.getCheerCnt() - 1);
                }
                cheerRepository.delete(cheer);
            }
        }

        // 참여자 있는 목표는 본인 정보만 삭제시키기 
        List<Goal> goals = goalRepository.findAllByMember(member);
        if(!goals.isEmpty()) {
            for (Goal goal : goals) {
                goalService.deleteGoal(goal);
            }
        }
        // 멤버 삭제
        memberRepository.delete(member);
    }

    @Transactional
    public void deleteMember() {
        Member member = isMemberCurrent(memberRepository);
        memberRepository.delete(member);
    }
}
