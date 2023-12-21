package com.goalkeepers.server.service;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.goalkeepers.server.config.SecurityUtil;
import com.goalkeepers.server.dto.PostRequestDto;
import com.goalkeepers.server.dto.PostResponseDto;
import com.goalkeepers.server.entity.Goal;
import com.goalkeepers.server.entity.Member;
import com.goalkeepers.server.entity.Post;
import com.goalkeepers.server.repository.GoalRepository;
import com.goalkeepers.server.repository.MemberRepository;
import com.goalkeepers.server.repository.PostRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class BoardService {
    
    private final PostRepository postRepository;
    private final GoalRepository goalRepository;
    private final MemberRepository memberRepository;

    // 모든 게시글 보기
    public List<PostResponseDto> getAllPostList() {
        return postRepository.findAllByOrderByUpdatedAtDesc()
                .stream()
                .map(PostResponseDto::of)
                .collect(Collectors.toList());
    }

    /*
     *  게시글 쓰기
        게시글 수정하기
        게시글 삭제하기
     */

    public PostResponseDto createMyPost(PostRequestDto requestDto) {
        Member member = isMemberCurrent();
        Goal goal = goalRepository.findById(requestDto.getGoalId())
                        .orElseThrow(() -> new RuntimeException("Goal Id를 확인해주세요."));
        if(goal.getMember().equals(member)) {
            Post post = requestDto.toPost(member, goal);
            return PostResponseDto.of(postRepository.save(post));
        } else {
            throw new RuntimeException("Goal을 만든 유저와 Post를 만든 유저가 다릅니다.");
        }        
    }

    public PostResponseDto updateMyPost(PostRequestDto requestDto, Long postId) {
        Member member = isMemberCurrent();
        Post post = postRepository.findById(postId)
                    .orElseThrow(() -> new RuntimeException("Post Id를 확인해주세요."));
    
        if(post.getMember().equals(member)) {
            return PostResponseDto.of(Post.postUpdate(post, requestDto));
        } else {
            throw new RuntimeException("로그인한 유저와 작성 유저가 같지 않습니다.");
        }
    }

    public void deleteMyPost(Long postId) {
        Member member = isMemberCurrent();
        Post post = postRepository.findById(postId)
                    .orElseThrow(() -> new RuntimeException("Post Id를 확인해주세요."));
        
        if(post.getMember().equals(member)) {
            postRepository.delete(post);
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

