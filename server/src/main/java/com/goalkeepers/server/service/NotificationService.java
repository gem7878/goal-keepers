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
import lombok.extern.slf4j.Slf4j;

@Slf4j
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
            System.out.println("!lastEventId.isEmpty()");
            Map<String, Object> events = emitterRepository.findAllEventCacheStartWithByMemberId(String.valueOf(memberId));
            System.out.println(events);
            events.entrySet().stream()
                    .filter((entry) -> lastEventId.compareTo(entry.getKey()) < 0)
                    .forEachOrdered((entry) -> sendToClient(emitter, entry.getKey(), emitterId, entry.getValue()));
        }

        return emitter;
    }

    private void sendToClient(SseEmitter emitter, String eventId, String emitterId, Object data) {
        try {
            System.out.println(eventId);
            emitter.send(SseEmitter.event()
                                    .id(eventId)
                                    .name("sse")
                                    .data(data));
        } catch (IOException e) {
            log.error("SSE 연결 닫힘: " + emitterId + " / " + eventId);
        } catch (IllegalStateException e) {
            log.error("IllegalStateException");
        }
    }

    public void send(Member receiver, Member giver, TYPE type, Long targetId, String targetTitle, Long commentId) {
        Notification notification = notificationRepository.findByReceiverAndGiverAndTypeAndTargetIdAndCommentId(receiver, giver, type, targetId, commentId)
                                        .orElseGet(() -> {
                                            return notificationRepository.save(new Notification(receiver, giver, type, targetId, commentId));
                                        });
        
        String receiverId = String.valueOf(receiver.getId());
        String eventId = receiverId + "_" + System.currentTimeMillis();

        Map<String, SseEmitter> sseEmitters = emitterRepository.findAllEmitterStartWithByMemberId(receiverId);
        sseEmitters.forEach(
            (key, emitter) -> {
                NotificationResponseDto notificationResponseDto = NotificationResponseDto.of(notification, targetTitle, commentId);
                emitterRepository.saveEventCache(eventId, notificationResponseDto);
                sendToClient(emitter, eventId, key, notificationResponseDto);
            }
        );
    }
}
