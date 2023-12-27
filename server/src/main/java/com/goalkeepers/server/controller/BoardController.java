package com.goalkeepers.server.controller;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.goalkeepers.server.dto.CommentRequestDto;
import com.goalkeepers.server.dto.CommentResponseDto;
import com.goalkeepers.server.dto.CommonResponseDto;
import com.goalkeepers.server.dto.GoalResponseDto;
import com.goalkeepers.server.dto.GoalShareRequestDto;
import com.goalkeepers.server.dto.PostLikeRequestDto;
import com.goalkeepers.server.dto.PostListPageResponseDto;
import com.goalkeepers.server.dto.PostRequestDto;
import com.goalkeepers.server.dto.PostResponseDto;
import com.goalkeepers.server.service.BoardService;
import com.goalkeepers.server.service.CommentService;
import com.goalkeepers.server.service.GoalService;
import com.goalkeepers.server.service.LikeShareService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/board")
public class BoardController {

    private final BoardService boardService;
    private final CommentService commentService;
    private final LikeShareService likeShareService;
    private final GoalService goalService;

    /*
     *  여러 유저의 포스트 보기
        좋아요
        담기
     */

    @GetMapping("/all")
    public ResponseEntity<CommonResponseDto> getAllPost(@RequestParam(name = "page") int pageNumber) {
        Page<PostListPageResponseDto> response = boardService.getAllPostList(pageNumber);
        return ResponseEntity.ok(new CommonResponseDto(true, response));
    }

    @PostMapping("/post/like")
    public ResponseEntity<CommonResponseDto> postlike(@Valid @RequestBody PostLikeRequestDto requestDto) {
        String response = likeShareService.addLike(requestDto);
        return ResponseEntity.ok(new CommonResponseDto(true, "Post " + requestDto.getPostId() + response));
    }

    // 연결된 골 찾기
    @GetMapping("/goal/share")
    public ResponseEntity<CommonResponseDto> findMyGoalWithShare(@RequestParam(name = "goal-id") Long goalId) {
        GoalResponseDto response = likeShareService.findGoal(goalId);
        return ResponseEntity.ok(new CommonResponseDto(true, response));
    }

    // 공유하기 -> 골 만들기
    @PostMapping("/goal/share")
    public ResponseEntity<CommonResponseDto> postShare(@Valid @RequestBody GoalShareRequestDto requestDto) {
        likeShareService.addShare(requestDto);
        return ResponseEntity.ok(new CommonResponseDto(true, "Goal " + requestDto.getGoalId() + " 담기 성공"));
    }

    // 공유 취소하기 -> 골 삭제하기
    @DeleteMapping("/goal/share")
    public ResponseEntity<CommonResponseDto> deleteShare(@Valid @RequestBody GoalShareRequestDto requestDto) {
        goalService.deleteMyGoal(requestDto.getGoalId());
        return ResponseEntity.ok(new CommonResponseDto(true, "Goal " + requestDto.getGoalId() + " 삭제 성공"));
    }

    /*
     *  게시글 쓰기
        게시글 수정하기
        게시글 삭제하기
     */

    @PostMapping("/post")
    public ResponseEntity<CommonResponseDto> createPost(@Valid @RequestBody PostRequestDto requestDto) {
        PostResponseDto response = boardService.createMyPost(requestDto);
        return ResponseEntity.ok(new CommonResponseDto(true, response));
    }

    @PutMapping("/post")
    public ResponseEntity<CommonResponseDto> updateMyPost(@RequestParam(name = "post-id") Long postId, @Valid @RequestBody PostRequestDto requestDto) {
        PostResponseDto response = boardService.updateMyPost(requestDto, postId);
        return ResponseEntity.ok(new CommonResponseDto(true, response));
    }

    @DeleteMapping("/post")
    public ResponseEntity<CommonResponseDto> deleteMyPost(@RequestParam(name = "post-id") Long postId) {
        boardService.deleteMyPost(postId);
        return ResponseEntity.ok(new CommonResponseDto(true, postId + " 게시글을 삭제하였습니다."));
    }

    /*
     *  게시글 상세보기 (전체 댓글 보기)
        댓글 쓰기
        댓글 삭제
        댓글 수정
     */

    @GetMapping("/all-comment")
    public ResponseEntity<CommonResponseDto> getAllComment(@RequestParam(name = "page") int pageNumber, @RequestParam(name = "post-id") Long postId) {
        Page<CommentResponseDto> response = commentService.getSelectedPost(postId, pageNumber);
        return ResponseEntity.ok(new CommonResponseDto(true, response));
    }

    @PostMapping("/comment")
    public ResponseEntity<CommonResponseDto> createMyComment(@RequestParam(name = "post-id") Long postId, @Valid @RequestBody CommentRequestDto requestDto) {
        CommentResponseDto response = commentService.createMyComment(requestDto, postId);
        return ResponseEntity.ok(new CommonResponseDto(true, response));
    }

    @PutMapping("/comment")
    public ResponseEntity<CommonResponseDto> updateMyComment(@RequestParam(name = "comment-id") Long commentId, @Valid @RequestBody CommentRequestDto requestDto) {
        CommentResponseDto response = commentService.updateMyComment(requestDto, commentId);
        return ResponseEntity.ok(new CommonResponseDto(true, response));
    }

    @DeleteMapping("/comment")
    public ResponseEntity<?> deleteMyComment(@RequestParam(name = "comment-id") Long commentId) {
        commentService.deleteMyComment(commentId);
        return ResponseEntity.ok(new CommonResponseDto(true, commentId + " 댓글을 삭제하였습니다."));
    }
}
