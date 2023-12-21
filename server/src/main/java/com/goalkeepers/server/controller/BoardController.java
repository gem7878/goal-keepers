package com.goalkeepers.server.controller;

import java.util.List;

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
import com.goalkeepers.server.dto.GoalShareRequestDto;
import com.goalkeepers.server.dto.PostLikeRequestDto;
import com.goalkeepers.server.dto.PostRequestDto;
import com.goalkeepers.server.dto.PostResponseDto;
import com.goalkeepers.server.service.BoardService;
import com.goalkeepers.server.service.CommentService;
import com.goalkeepers.server.service.LikeShareService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/board")
public class BoardController {

    private final BoardService boardService;
    private final CommentService commentService;
    private final LikeShareService likeShareService;

    /*
     *  여러 유저의 포스트 보기
        담기
        좋아요
     */

    @GetMapping("/all")
    public ResponseEntity<?> getAllPost() {
        return ResponseEntity.ok(boardService.getAllPostList());
    }

    @PostMapping("/post/like")
    public ResponseEntity<?> postlike(@RequestBody PostLikeRequestDto requestDto) {
        try {
            likeShareService.addLike(requestDto);
            return ResponseEntity.ok("Post " + requestDto.getPostId() + " 좋아요 성공");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("좋아요 실패하였습니다.");
        }
    }

    @PostMapping("/goal/share")
    public ResponseEntity<?> postShare(@RequestBody GoalShareRequestDto requestDto) {
        try {
            likeShareService.addShare(requestDto);
            return ResponseEntity.ok("Goal " + requestDto.getGoalId() + " 담기 성공");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("담기 실패하였습니다.");
        }
    }

    /*
     *  게시글 쓰기
        게시글 수정하기
        게시글 삭제하기
     */

    @PostMapping("/post")
    public ResponseEntity<?> createPost(@RequestBody PostRequestDto requestDto) {
        try {
            PostResponseDto response = boardService.createMyPost(requestDto);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("게시글 작성에 실패하였습니다.");
        }
    }

    @PutMapping("/post")
    public ResponseEntity<?> updateMyPost(@RequestParam(name = "post-id") Long postId, @RequestBody PostRequestDto requestDto) {
        try {
            PostResponseDto response = boardService.updateMyPost(requestDto, postId);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("게시글 수정에 실패하였습니다.");
        }
    }

    @DeleteMapping("/post")
    public ResponseEntity<?> deleteMyPost(@RequestParam(name = "post-id") Long postId) {
        try {
            boardService.deleteMyPost(postId);
            return ResponseEntity.ok(postId + " 게시글을 삭제하였습니다.");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("게시글 삭제를 실패했습니다.");
        }
    }

    /*
     *  게시글 상세보기 (전체 댓글 보기)
        댓글 쓰기
        댓글 삭제
        댓글 수정
     */

    @GetMapping("/all-comment")
    public ResponseEntity<?> getAllComment(@RequestParam(name = "post-id") Long postId) {
        try {
            List<CommentResponseDto> response = commentService.getSelectedPost(postId);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("댓글을 불러오는데 실패했습니다.");
        }
    }

    @PostMapping("/comment")
    public ResponseEntity<?> createMyComment(@RequestParam(name = "post-id") Long postId, @RequestBody CommentRequestDto requestDto) {
        try {
            CommentResponseDto response = commentService.createMyComment(requestDto, postId);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("댓글을 불러오는데 실패했습니다.");
        }
    }

    @PutMapping("/comment")
    public ResponseEntity<?> updateMyComment(@RequestParam(name = "comment-id") Long commentId, @RequestBody CommentRequestDto requestDto) {
        try {
            CommentResponseDto response = commentService.updateMyComment(requestDto, commentId);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("댓글 수정을 실패했습니다.");
        }
    }

    @DeleteMapping("/comment")
    public ResponseEntity<?> deleteMyComment(@RequestParam(name = "comment-id") Long commentId) {
        try {
            commentService.deleteMyComment(commentId);
            return ResponseEntity.ok(commentId + " 댓글을 삭제하였습니다.");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("댓글 삭제를 실패했습니다.");
        }
    }
}
