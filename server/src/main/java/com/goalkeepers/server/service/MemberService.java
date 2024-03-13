package com.goalkeepers.server.service;

import java.util.Optional;
import java.util.Objects;

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
import com.goalkeepers.server.repository.MemberRepository;
import com.goalkeepers.server.repository.PostContentRepository;
import com.goalkeepers.server.repository.PostRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MemberService extends ServiceHelper {
    
    private final MemberRepository memberRepository;
    private final GoalRepository goalRepository;
    private final PostContentRepository contentRepository;
    private final PostRepository postRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthService authService;

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

    // 탈퇴
    @Transactional
    public void deleteMember(String email, String password) {
        Member member = isMemberCurrent(memberRepository);

        if(!member.getEmail().equals(email)) {
            throw new CustomException(ErrorCode.BAD_REQUEST, "이메일을 확인해주세요.");
        }

        if (!passwordEncoder.matches(password, member.getPassword())) {
            throw new CustomException(ErrorCode.BAD_REQUEST, "비밀번호를 확인해주세요.");
        }

        // 쉐어 카운트 -1
        for (GoalShare share : member.getShares()) {
            Optional<Goal> goal = goalRepository.findById(share.getGoal().getId());
            if(goal.isPresent()) {
                goal.get().setShareCnt(goal.get().getShareCnt() - 1);
            }
        }

        // 라이크 카운트 -1
        for (PostLike like : member.getLikes()) {
            Optional<PostContent> content = contentRepository.findById(like.getPostContent().getId());
            if(content.isPresent()) {
                content.get().setLikeCnt(content.get().getLikeCnt() - 1);
            }
        }

        // 응원해요 카운트 -1
        for (PostCheer cheer : member.getCheers()) {
            Optional<Post> post = postRepository.findById(cheer.getPost().getId());
            if(post.isPresent()) {
                post.get().setCheerCnt(post.get().getCheerCnt() - 1);
            }
        }

        // 참여자 있는 목표는 본인 정보만 삭제시키기 
        for (Goal goal : member.getGoals()) {
            if(Objects.isNull(goal.getShare()) && Objects.nonNull(goal.getShareList())) {
                goal.setMember(null);
                Post post = postRepository.findByGoal(goal).orElse(null);
                if(Objects.nonNull(post)) {
                    postRepository.delete(post);
                }
            }
        }
        
        memberRepository.delete(member);
    }
}
