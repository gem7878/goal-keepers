package com.goalkeepers.server.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.goalkeepers.server.config.SecurityUtil;
import com.goalkeepers.server.dto.PostListPageResponseDto;
import com.goalkeepers.server.dto.PostRequestDto;
import com.goalkeepers.server.dto.PostResponseDto;
import com.goalkeepers.server.entity.Goal;
import com.goalkeepers.server.entity.Member;
import com.goalkeepers.server.entity.Post;
import com.goalkeepers.server.entity.PostLike;
import com.goalkeepers.server.repository.GoalRepository;
import com.goalkeepers.server.repository.GoalShareRepository;
import com.goalkeepers.server.repository.MemberRepository;
import com.goalkeepers.server.repository.PostLikeRepository;
import com.goalkeepers.server.repository.PostRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class BoardService extends CommonService {
    
    private final PostRepository postRepository;
    private final GoalRepository goalRepository;
    private final MemberRepository memberRepository;
    private final GoalShareRepository shareRepository;
    private final PostLikeRepository likeRepository;

    // 모든 게시글 보기
    public List<PostListPageResponseDto> getAllPostList() {
        Long memberId = SecurityUtil.getCurrentMemberId();
        if (memberId == null) {
            return postRepository.findAllByOrderByUpdatedAtDesc()
                .stream()
                .map(post -> PostListPageResponseDto.of(post, false, false))
                .collect(Collectors.toList());
        } else {
            Optional<Member> member = memberRepository.findById(memberId);
            return postRepository.findAllByOrderByUpdatedAtDesc()
                .stream()
                .map(post -> {
                    boolean isLike = likeRepository.existsByMemberAndPost(member.get(), post);
                    boolean isShare = post.getGoal() != null ? shareRepository.existsByMemberAndGoal(member.get(), post.getGoal()) : false;
                    return PostListPageResponseDto.of(post, isLike, isShare);
                })
                .collect(Collectors.toList());
        }
    }

    /*
     *  게시글 쓰기
        게시글 수정하기
        게시글 삭제하기
     */

    public PostResponseDto createMyPost(PostRequestDto requestDto) {
        Member member = isMemberCurrent(memberRepository);
        Goal goal = isMyGoal(memberRepository, goalRepository, requestDto.getGoalId());
        Post post = requestDto.toPost(member, goal);
        return PostResponseDto.of(postRepository.save(post));     
    }

    public PostResponseDto updateMyPost(PostRequestDto requestDto, Long postId) {  
        return PostResponseDto.of(Post.postUpdate(isMyPost(memberRepository, postRepository, postId), requestDto));
    }

    public void deleteMyPost(Long postId) {
        Post post = isMyPost(memberRepository, postRepository, postId);
        
        List<PostLike> likeList = likeRepository.findAllByPost(post);
        for (PostLike like : likeList) {
            likeRepository.delete(like);
        }
        postRepository.delete(isMyPost(memberRepository, postRepository, postId));
    }
}

