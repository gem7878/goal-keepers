package com.goalkeepers.server.controller;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.goalkeepers.server.dto.CommonResponseDto;
import com.goalkeepers.server.dto.DeleteAlarmRequestDto;
import com.goalkeepers.server.dto.NotificationResponseDto;
import com.goalkeepers.server.dto.TargetRequestDto;
import com.goalkeepers.server.dto.TargetResponseDto;
import com.goalkeepers.server.entity.TYPE;
import com.goalkeepers.server.service.GoalService;
import com.goalkeepers.server.service.NotificationService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
public class NotificationController {

    private final NotificationService notificationService;
    private final GoalService goalService;

    @GetMapping(value = "/alarm")
    public ResponseEntity<CommonResponseDto> getAlarms(@RequestParam(value = "type") TYPE type,
                                                        @RequestParam(value = "page") int pageNumber) {
        Page<NotificationResponseDto> response = notificationService.getAlarms(type, pageNumber);
        return ResponseEntity.ok(new CommonResponseDto(true, response));
    }

    @PatchMapping(value = "/alarm")
    public ResponseEntity<CommonResponseDto> readAlarms() {
        Long number = notificationService.ChangeReadAlarms();
        return ResponseEntity.ok(new CommonResponseDto(true, number + "개의 알람이 모두 읽음 처리되었습니다."));
    }

    @DeleteMapping(value = "/alarm")
    public ResponseEntity<CommonResponseDto> deleteAlarms(@Valid @RequestBody DeleteAlarmRequestDto requestDto) {
        Long number = notificationService.deleteAlarms(requestDto);
        return ResponseEntity.ok(new CommonResponseDto(true, number + "개의 알람이 삭제되었습니다."));
    }

    @PostMapping(value = "/alarm/target")
    public ResponseEntity<CommonResponseDto> findTarget(@RequestBody TargetRequestDto requestDto) {
        TargetResponseDto response = notificationService.findTarget(requestDto);
        return ResponseEntity.ok(new CommonResponseDto(true, response));
    }

    /*
     * SSE
     */
    @GetMapping(value = "/subscribe", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public SseEmitter subscribe(@RequestHeader(value = "Last-Event-ID", required = false, defaultValue = "") String lastEventId) {
        return notificationService.subscribe(lastEventId);
    }

    /*
     * 일주일/하루 디데이 알림 스케줄링
     */
    @Scheduled(cron = "0 0 9 * * ?")
    public void ddayAlarm() {
        goalService.notifyOneWeekLeftGoal();
        goalService.notifyOneDayLeftGoal();
        // 당일
    }

    /*
     * 오늘 목표를 이룬 사람 수 전체 알림 스케줄링
     */
    @Scheduled(cron = "0 0 0 * * ?")
    public void todayAlarm() {
        goalService.notifyCompletedGoalNumber();
    }
}
