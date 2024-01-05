package com.goalkeepers.server.controller;

import static com.goalkeepers.server.entity.QGoal.goal;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.goalkeepers.server.dto.CommonResponseDto;
import com.goalkeepers.server.dto.GoalResponseDto;
import com.goalkeepers.server.service.GoalService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/public")
public class CommonController {

    private final GoalService goalService;
    
    @GetMapping("/goal")
    public ResponseEntity<CommonResponseDto> getOneGoal(@RequestParam(name = "id", required = true) Long goalId) {
        GoalResponseDto response = goalService.getSelectedGoal(goalId);
        return ResponseEntity.ok(new CommonResponseDto(true, response));
    }
}
