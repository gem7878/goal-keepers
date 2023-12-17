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
        버킷 삭제
        버킷 수정
     */

    private final GoalService goalService;

    @GetMapping("/me")
    public ResponseEntity<List<GoalResponseDto>> getMyGoalLists() {
        return ResponseEntity.ok(goalService.getMyGoalList());
    }
    /* 버킷 상세 보기 추가해야됨 */
    /*@GetMapping("/me")
    public ResponseEntity<?> getMyGoal(@RequestParam(name = "id") Long id) {
        return ResponseEntity.ok("");
    }*/

    @PostMapping("/goal")
    public ResponseEntity<GoalResponseDto> createMyGoal(@RequestBody GoalRequestDto requestDto) {
        return ResponseEntity.ok(goalService.createMyGoal(requestDto));
    }

    @PutMapping("/goal")
    public ResponseEntity<GoalResponseDto> updateMyGoal(@RequestParam(name = "id") Long id, @RequestBody GoalRequestDto requestDto) {
        return ResponseEntity.ok(goalService.updateMyGoal(requestDto, id));
    }

    @DeleteMapping("/goal")
    public ResponseEntity<String> deleteMyGoal(@RequestParam(name = "id") Long id) {
        goalService.deleteMyGoal(id);
        return ResponseEntity.ok(id + " Goal을 삭제하였습니다.");
    }
    
}
