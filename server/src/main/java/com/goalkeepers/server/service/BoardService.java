package com.goalkeepers.server.service;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.goalkeepers.server.dto.PostListPageResponseDto;
import com.goalkeepers.server.dto.PostRequestDto;
import com.goalkeepers.server.dto.PostResponseDto;
import com.goalkeepers.server.entity.Goal;
import com.goalkeepers.server.entity.Member;
import com.goalkeepers.server.entity.Post;
import com.goalkeepers.server.entity.PostLike;
import com.goalkeepers.server.repository.GoalRepository;
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

    // 모든 게시글 보기
    public Page<PostListPageResponseDto> getAllPostList(int pageNumber) {
        return postRepository.searchAll(PageRequest.of(pageNumber - 1, 10));
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

