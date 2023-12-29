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

import com.goalkeepers.server.dto.CommonResponseDto;
import com.goalkeepers.server.dto.GoalRequestDto;
import com.goalkeepers.server.dto.GoalResponseDto;
import com.goalkeepers.server.service.GoalService;

import jakarta.validation.Valid;
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
    public ResponseEntity<CommonResponseDto> getMyGoalLists(@RequestParam(name = "page") int pageNumber) {
        Page<GoalResponseDto> response = goalService.getMyGoalList(pageNumber);
        return ResponseEntity.ok(new CommonResponseDto(true, response));
    }

    @PostMapping("/goal")
    public ResponseEntity<CommonResponseDto> createMyGoal(@Valid @RequestBody GoalRequestDto requestDto) {
        GoalResponseDto response = goalService.createMyGoal(requestDto);
        return ResponseEntity.ok(new CommonResponseDto(true, response));
    }

    @PutMapping("/goal")
    public ResponseEntity<CommonResponseDto> updateMyGoal(@RequestParam(name = "id", required = true) Long id, @Valid @RequestBody GoalRequestDto requestDto) {
        GoalResponseDto response = goalService.updateMyGoal(requestDto, id);
        return ResponseEntity.ok(new CommonResponseDto(true, response));
    }

    @DeleteMapping("/goal")
    public ResponseEntity<CommonResponseDto> deleteMyGoal(@RequestParam(name = "id", required = true) Long id) {
        goalService.deleteMyGoal(id);
        return ResponseEntity.ok(new CommonResponseDto(true, id + " Goal을 삭제하였습니다."));
    }
    
}
