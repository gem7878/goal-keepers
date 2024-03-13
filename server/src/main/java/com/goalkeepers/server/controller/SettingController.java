package com.goalkeepers.server.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.goalkeepers.server.dto.CommonResponseDto;
import com.goalkeepers.server.dto.SettingResponseDto;
import com.goalkeepers.server.dto.SettingUpdateRequestDto;
import com.goalkeepers.server.service.SettingService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequiredArgsConstructor
@RequestMapping("/setting")
public class SettingController {

    private final SettingService settingService;
    @GetMapping("/me")
    public ResponseEntity<CommonResponseDto> getMySettings() {
        SettingResponseDto response = settingService.getMySettings();
        return ResponseEntity.ok(new CommonResponseDto(true, response));
    }

    @PutMapping("/me")
    public ResponseEntity<CommonResponseDto> changeAlarm(@Valid @RequestBody SettingUpdateRequestDto requestDto) {
        settingService.changeAlarm(requestDto);
        return ResponseEntity.ok(new CommonResponseDto(true, "알림 설정이 변경되었습니다."));
    }

}
