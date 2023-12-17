// package com.goalkeepers.server.controller;

// import org.springframework.http.ResponseEntity;
// import org.springframework.web.bind.annotation.DeleteMapping;
// import org.springframework.web.bind.annotation.GetMapping;
// import org.springframework.web.bind.annotation.PostMapping;
// import org.springframework.web.bind.annotation.PutMapping;
// import org.springframework.web.bind.annotation.RequestBody;
// import org.springframework.web.bind.annotation.RequestMapping;
// import org.springframework.web.bind.annotation.RequestParam;
// import org.springframework.web.bind.annotation.RestController;

// import com.goalkeepers.server.dto.GoalRequestDto;

// import lombok.RequiredArgsConstructor;

// @RestController
// @RequiredArgsConstructor
// @RequestMapping("/community")
// public class CommunityController {

//     /*
//      *  여러 유저의 포스트 보기
//         담기
//         좋아요
//      */

//     @GetMapping("/post")
//     public ResponseEntity<?> getAllPost() {
//         return ResponseEntity.ok("");
//     }

//     @PostMapping("/post/like")
//     public ResponseEntity<?> postlike(@RequestParam(name = "id") Long id, @RequestBody ) {
//         return ResponseEntity.ok("");
//     }

//     @PostMapping("/post/share")
//     public ResponseEntity<?> postShare(@RequestParam(name = "id") Long id, @RequestBody ) {
//         return ResponseEntity.ok("");
//     }

//     /*
//      *  게시글 상세 보기
//         게시글 수정하기
//         게시글 삭제하기
//      */

//     @GetMapping("/post")
//     public ResponseEntity<?> getSelectedPost(@RequestParam(name = "id") Long id) {
//         return ResponseEntity.ok("");
//     }

//     @PutMapping("/post")
//     public ResponseEntity<?> updateMyPost(@RequestParam(name = "id") Long id, @RequestBody ) {
//         return ResponseEntity.ok("");
//     }

//     @DeleteMapping("/post")
//     public ResponseEntity<?> deleteMyPost(@RequestParam(name = "id") Long id) {
//         return ResponseEntity.ok("");
//     }

//     /*
//      *  전체 댓글 보기
//         댓글 쓰기
//         댓글 삭제
//         댓글 수정
//      */

//     @GetMapping("/comment")
//     public ResponseEntity<?> getAllComment(@RequestParam(name = "id") Long id) {
//         return ResponseEntity.ok("");
//     }

//     @PutMapping("/comment")
//     public ResponseEntity<?> updateMyComment(@RequestParam(name = "id") Long id, @RequestBody ) {
//         return ResponseEntity.ok("");
//     }

//     @DeleteMapping("/comment")
//     public ResponseEntity<?> deleteMyComment(@RequestParam(name = "id") Long id) {
//         return ResponseEntity.ok("");
//     }
// }
