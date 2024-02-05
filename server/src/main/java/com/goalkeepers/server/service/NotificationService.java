package com.goalkeepers.server.service;

import java.util.Objects;
import java.lang.Object;
import java.io.IOException;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.goalkeepers.server.config.SecurityUtil;
import com.goalkeepers.server.dto.NotificationResponseDto;
import com.goalkeepers.server.entity.Member;
import com.goalkeepers.server.entity.Notification;
import com.goalkeepers.server.entity.TYPE;
import com.goalkeepers.server.exception.CustomException;
import com.goalkeepers.server.repository.EmitterRepository;
import com.goalkeepers.server.repository.NotificationRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class NotificationService {

    private static final Long DEFAULT_TIMEOUT = 60L * 1000 * 60;

    private final EmitterRepository emitterRepository;
    private final NotificationRepository notificationRepository;

    public SseEmitter subscribe(String lastEventId) {
        Long memberId = SecurityUtil.getCurrentMemberId();
        if(Objects.isNull(memberId)) {
            throw new CustomException("로그인이 필요합니다.");
        }
        
        String emitterId = memberId + "_" + System.currentTimeMillis();
        SseEmitter emitter = emitterRepository.save(emitterId, new SseEmitter(DEFAULT_TIMEOUT));

        emitter.onCompletion(() -> emitterRepository.deleteById(emitterId));
        emitter.onTimeout(() -> emitterRepository.deleteById(emitterId));

        // 503 에러 방지 더미 데이터
        String eventId = memberId + "_" + System.currentTimeMillis();
        sendToClient(emitter, eventId, emitterId, "EventStream Created. [memberId=" + memberId + "]");

        if(!lastEventId.isEmpty()) {
            Map<String, Object> events = emitterRepository.findAllEventCacheStartWithByMemberId(String.valueOf(memberId));
            events.entrySet().stream()
                    .filter((entry) -> lastEventId.compareTo(entry.getKey()) < 0)
                    .forEach((entry) -> sendToClient(emitter, entry.getKey(), emitterId, entry.getValue()));
        }

        return emitter;
    }

    private void sendToClient(SseEmitter emitter, String eventId, String emitterId, Object data) {
        try {
            emitter.send(SseEmitter.event()
                                    .id(eventId)
                                    .name("sse")
                                    .data(data));
        } catch (IOException e) {
            emitterRepository.deleteById(emitterId);
            throw new RuntimeException("연결 오류");
        }
    }

    public void send(Member member, TYPE type, String message, Long targetId) {
        Notification notification = notificationRepository.save(new Notification(member, type, message, targetId));
        
        String receiverId = String.valueOf(member.getId());
        String eventId = receiverId + "_" + System.currentTimeMillis();

        Map<String, SseEmitter> sseEmitters = emitterRepository.findAllEmitterStartWithByMemberId(receiverId);
        sseEmitters.forEach(
            (key, emitter) -> {
                emitterRepository.saveEventCache(key, notification);
                sendToClient(emitter, eventId, key, NotificationResponseDto.of(notification));
            }
        );
    }
}
