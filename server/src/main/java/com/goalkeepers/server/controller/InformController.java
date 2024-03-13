package com.goalkeepers.server.controller;

import org.springframework.web.bind.annotation.RestController;

import com.goalkeepers.server.dto.CommonResponseDto;
import com.goalkeepers.server.dto.InformRequestDto;
import com.goalkeepers.server.service.InformService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;



@RequiredArgsConstructor
@RestController
public class InformController {

    private final InformService informService;

    /*
     * 공지사항 생성, 삭제
     */

    @PostMapping("/admin/inform")
    public ResponseEntity<CommonResponseDto> createInform(@Valid @RequestBody InformRequestDto requestDto) {
        Long id = informService.createInform(requestDto);
        return ResponseEntity.ok(new CommonResponseDto(true, id + " 공지를 작성하였습니다."));
    }

    @DeleteMapping("/admin/inform")
    public ResponseEntity<CommonResponseDto> deleteInform(@RequestParam(value = "id") Long id) {
        informService.deleteInform(id);
        return ResponseEntity.ok(new CommonResponseDto(true, id + " 공지를 삭제하였습니다."));
    }
}
