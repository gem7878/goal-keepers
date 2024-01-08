package com.goalkeepers.server.service;

import java.util.Objects;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.goalkeepers.server.config.SecurityUtil;
import com.goalkeepers.server.dto.PostListPageResponseDto;
import com.goalkeepers.server.dto.PostRequestDto;
import com.goalkeepers.server.dto.PostResponseDto;
import com.goalkeepers.server.entity.Goal;
import com.goalkeepers.server.entity.Member;
import com.goalkeepers.server.entity.Post;
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
    private final PostLikeRepository likeRepository;
    private final GoalShareRepository shareRepository;
    private final LikeShareService likeShareService;
    private final FirebaseStorageService firebaseStorageService;

    /*
     * 모든 게시글 가져오기
     * 나의 모든 게시글 가져오기
     * 하나의 게시글 가져오기
     */

    public Page<PostListPageResponseDto> getAllPostList(int pageNumber) {
        return postRepository.searchAll(PageRequest.of(pageNumber - 1, 10));
    }

    public Page<PostListPageResponseDto> getMyAllPostList(int pageNumber) {
        Member member = isMemberCurrent(memberRepository);
        return postRepository.searchMyAllPost(PageRequest.of(pageNumber - 1, 10), member);
    }

    public PostListPageResponseDto getOnePost(Long postId) {
        Member member = memberRepository.findById(SecurityUtil.getCurrentMemberId()).orElse(null);
        boolean isLike = false;
        boolean isShare = false;
        Post post = isPost(postRepository, postId);
        Goal goal = post.getGoal();
        if(Objects.nonNull(member)) {
            isLike = likeRepository.existsByMemberAndPost(member, post);
            isShare = shareRepository.existsByMemberAndGoal(member, goal);
        }
        return PostListPageResponseDto.of(post, getGoalImageUrl(goal), isLike, isShare);
    }

    /*
     * 게시글 쓰기
     * 게시글 수정하기
     * 게시글 삭제하기
     */

    public PostResponseDto createMyPost(PostRequestDto requestDto) {
        Member member = isMemberCurrent(memberRepository);
        Goal goal = isMyGoal(memberRepository, goalRepository, requestDto.getGoalId());
        Post post = requestDto.toPost(member, goal);
        return PostResponseDto.of(postRepository.save(post), getGoalImageUrl(goal));    
    }

    public PostResponseDto updateMyPost(PostRequestDto requestDto, Long postId) {
        Post post = Post.postUpdate(isMyPost(memberRepository, postRepository, postId), requestDto);
        Goal goal = post.getGoal();
        return PostResponseDto.of(post, getGoalImageUrl(goal));
    }

    public void deleteMyPost(Long postId) {
        Post post = isMyPost(memberRepository, postRepository, postId);
        likeShareService.deleteLike(post);
        postRepository.delete(post);
    }

    private String getGoalImageUrl(Goal goal) {
        String imageUrl = Objects.nonNull(goal)? goal.getImageUrl() : null;
        if(Objects.nonNull(imageUrl) && !imageUrl.isEmpty()) {
            imageUrl = firebaseStorageService.showFile(imageUrl);
        }
        return imageUrl;
    }
}

