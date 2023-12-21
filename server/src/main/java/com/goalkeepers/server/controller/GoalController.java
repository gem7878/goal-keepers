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

import com.goalkeepers.server.dto.GoalRequestDto;
import com.goalkeepers.server.dto.GoalResponseDto;
import com.goalkeepers.server.dto.PostResponseDto;
import com.goalkeepers.server.service.GoalService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/goal-list")
public class GoalController {
    
    /*
     *  나의 전체 버킷리스트 보기
        버킷 상세 보기
        버킷 생성
        버킷 수정
        버킷 삭제
     */

    private final GoalService goalService;

    @GetMapping("/all")
    public ResponseEntity<?> getMyGoalLists() {
        try {
            List<GoalResponseDto> response = goalService.getMyGoalList();
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("나의 Goal 정보를 불러오는데 실패했습니다.");
        }
        
    }

    @GetMapping("/goal")
    public ResponseEntity<?> getMyGoal(@RequestParam(name = "id") Long id) {
        try {
            List<PostResponseDto> response = goalService.getSelectedGoal(id);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("나의 Goal 정보를 불러오는데 실패했습니다.");
        }
    }

    @PostMapping("/goal")
    public ResponseEntity<?> createMyGoal(@RequestBody GoalRequestDto requestDto) {
        try {
            GoalResponseDto response = goalService.createMyGoal(requestDto);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("나의 Goal 생성에 실패하였습니다.");
        }
    }

    @PutMapping("/goal")
    public ResponseEntity<?> updateMyGoal(@RequestParam(name = "id") Long id, @RequestBody GoalRequestDto requestDto) {
        try {
            GoalResponseDto response = goalService.updateMyGoal(requestDto, id);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("나의 Goal 수정에 실패하였습니다.");
        }
    }

    @DeleteMapping("/goal")
    public ResponseEntity<String> deleteMyGoal(@RequestParam(name = "id") Long id) {
        try {
            goalService.deleteMyGoal(id);
            return ResponseEntity.ok(id + " Goal을 삭제하였습니다.");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("나의 Goal 삭제에 실패하였습니다.");
        }
    }
    
}
