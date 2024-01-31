package com.goalkeepers.server.controller;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.goalkeepers.server.dto.CommonResponseDto;
import com.goalkeepers.server.dto.PostContentResponseDto;
import com.goalkeepers.server.dto.PostLikeRequestDto;
import com.goalkeepers.server.dto.PostRequestDto;
import com.goalkeepers.server.service.ContentService;
import com.goalkeepers.server.service.LikeShareService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class ContentController {
    private final ContentService contentService;
    private final LikeShareService likeShareService;

    /*
     *  게시글 쓰기
        게시글 수정하기
        게시글 삭제하기
     */

    @PostMapping("/post")
    public ResponseEntity<CommonResponseDto> createPost(@Valid @RequestBody PostRequestDto requestDto) {
        Long id = contentService.createMyPostContent(requestDto);
        return ResponseEntity.ok(new CommonResponseDto(true, id + " 게시글을 생성하였습니다."));
    }

    // @PutMapping("/post")
    // public ResponseEntity<CommonResponseDto> updateMyPost(@RequestParam(name = "content-id") Long contentId, @Valid @RequestBody PostContentUpdateRequestDto requestDto) {
    //     contentService.updateMyPostContent(requestDto, contentId);
    //     return ResponseEntity.ok(new CommonResponseDto(true, contentId + " 게시글을 수정하였습니다."));
    // }

    @DeleteMapping("/post")
    public ResponseEntity<CommonResponseDto> deleteMyPost(@RequestParam(name = "content-id") Long contentId) {
        contentService.deleteMyPostContent(contentId);
        return ResponseEntity.ok(new CommonResponseDto(true, contentId + " 게시글을 삭제하였습니다."));
    }

    /*
     * 포스트의 모든 컨텐트 가져오기
     */
    
    @GetMapping("/post/contents")
    public ResponseEntity<CommonResponseDto> getOnePost(@RequestParam(name = "post-id") Long postId,
                                                        @RequestParam(name = "page") int pageNumber) {
        Page<PostContentResponseDto> response = contentService.getPostContents(postId, pageNumber);
        return ResponseEntity.ok(new CommonResponseDto(true, response));
    }

    @GetMapping("/community/contents")
    public ResponseEntity<CommonResponseDto> getOneCommunity(@RequestParam(name = "goal-id") Long goalId,
                                                        @RequestParam(name = "page") int pageNumber) {
        Page<PostContentResponseDto> response = contentService.getCommunityContents(goalId, pageNumber);
        return ResponseEntity.ok(new CommonResponseDto(true, response));
    }

    /*
     * 컨텐트 좋아요
     */

    @PostMapping("/post/like")
    public ResponseEntity<CommonResponseDto> postlike(@Valid @RequestBody PostLikeRequestDto requestDto) {
        String response = likeShareService.addLike(requestDto);
        return ResponseEntity.ok(new CommonResponseDto(true, "Post Content " + requestDto.getContentId() + response));
    }

}
