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
import com.goalkeepers.server.dto.CommunityResponseDto;
import com.goalkeepers.server.dto.PostMyResponseDto;
import com.goalkeepers.server.dto.PostResponseDto;
import com.goalkeepers.server.service.BoardService;
import com.goalkeepers.server.service.CommentService;
import com.goalkeepers.server.service.ContentService;
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
    private final ContentService contentService;

    
    /*
     * 모든 포스트 가져오기
     * 나의 모든 포스트 가져오기
     */

    @GetMapping("/all")
    public ResponseEntity<CommonResponseDto> getAllPost(@RequestParam(name = "page") int pageNumber,
                                                        @RequestParam(name = "sort") String sort) {
        Page<PostResponseDto> response = contentService.getAllPost(pageNumber, sort);
        return ResponseEntity.ok(new CommonResponseDto(true, response));
    }

    @GetMapping("/all/me")
    public ResponseEntity<CommonResponseDto> getMyAllPost(@RequestParam(name = "page") int pageNumber,
                                                        @RequestParam(name = "sort") String sort) {
        Page<PostMyResponseDto> response = contentService.getMyAllPost(pageNumber, sort);
        return ResponseEntity.ok(new CommonResponseDto(true, response));
    }

    /*
     * 커뮤니티 최신순 가져오기
     * 커뮤니티 인기순 가져오기
     */

    @GetMapping("/community/new")
    public ResponseEntity<CommonResponseDto> getAllNewGoal(@RequestParam(name = "page", required = true) int pageNumber) {
        Page<CommunityResponseDto> response = boardService.getAllNewGoal(pageNumber);
        return ResponseEntity.ok(new CommonResponseDto(true, response));
    }

    @GetMapping("/community/popular")
    public ResponseEntity<CommonResponseDto> getAllPopularGoal(@RequestParam(name = "page", required = true) int pageNumber) {
        Page<CommunityResponseDto> response = boardService.getAllPopularGoal(pageNumber);
        return ResponseEntity.ok(new CommonResponseDto(true, response));
    }

    /*
     * Post Menu 검색
     * Community Menu 검색
     */

    @GetMapping("/search")
    public ResponseEntity<CommonResponseDto> searchPost(@RequestParam(name = "page", required = true) int pageNumber,
                                                                @RequestParam(name = "query", required = true) String query,
                                                                @RequestParam(name = "sort", required = false) String sort) {
        Page<PostResponseDto> response = boardService.searchPost(pageNumber, query, sort);
        return ResponseEntity.ok(new CommonResponseDto(true, response));
    }

    @GetMapping("/search")
    public ResponseEntity<CommonResponseDto> searchCommunity(@RequestParam(name = "page", required = true) int pageNumber,
                                                                @RequestParam(name = "query", required = true) String query,
                                                                @RequestParam(name = "sort", required = false) String sort) {
        Page<CommunityResponseDto> response = boardService.searchCommunity(pageNumber, query, sort);
        return ResponseEntity.ok(new CommonResponseDto(true, response));
    }    

    /*
     * 포스트 응원해요
     */

    // @PostMapping("/post/like")
    // public ResponseEntity<CommonResponseDto> postlike(@Valid @RequestBody PostLikeRequestDto requestDto) {
    //     String response = likeShareService.addLike(requestDto);
    //     return ResponseEntity.ok(new CommonResponseDto(true, "Post Content " + requestDto.getContentId() + response));
    // }

    /*
     *  전체 댓글 보기
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
