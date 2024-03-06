package com.goalkeepers.server.service;

import java.util.Objects;
import java.lang.Object;
import java.io.IOException;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.goalkeepers.server.common.ServiceHelper;
import com.goalkeepers.server.config.SecurityUtil;
import com.goalkeepers.server.dto.DeleteAlarmRequestDto;
import com.goalkeepers.server.dto.NotificationResponseDto;
import com.goalkeepers.server.dto.TargetRequestDto;
import com.goalkeepers.server.dto.TargetResponseDto;
import com.goalkeepers.server.entity.Member;
import com.goalkeepers.server.entity.Notification;
import com.goalkeepers.server.entity.TYPE;
import com.goalkeepers.server.exception.CustomException;
import com.goalkeepers.server.repository.EmitterRepository;
import com.goalkeepers.server.repository.MemberRepository;
import com.goalkeepers.server.repository.NotificationRepository;
import com.goalkeepers.server.repository.PostContentRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class NotificationService extends ServiceHelper {

    private static final Long DEFAULT_TIMEOUT = 60L * 1000 * 30;

    private final EmitterRepository emitterRepository;
    private final NotificationRepository notificationRepository;
    private final MemberRepository memberRepository;
    private final PostContentRepository postContentRepository;

    public synchronized SseEmitter subscribe(String lastEventId) {
        Long memberId = SecurityUtil.getCurrentMemberId();
        if(Objects.isNull(memberId)) {
            throw new CustomException("로그인이 필요합니다.");
        }

        System.out.println(emitterRepository.findAllEmitters().size());
        
        String emitterId = memberId + "_" + System.currentTimeMillis();
        SseEmitter emitter = emitterRepository.save(emitterId, new SseEmitter(DEFAULT_TIMEOUT));

        log.info("SSE Emitter 생성됨: {}", emitterId);

        emitter.onCompletion(() -> {
            emitter.complete();
            emitterRepository.deleteAllEmitterStartWithMemberId(String.valueOf(memberId));
            log.info("SSE Emitter 연결 해제");
        });

        emitter.onTimeout(() -> {
            String newEmitterId = memberId + "_" + System.currentTimeMillis();
            emitterRepository.save(newEmitterId, new SseEmitter(DEFAULT_TIMEOUT));
            emitterRepository.deleteById(emitterId);
            log.info("SSE Emitter 타임 아웃: {}", emitterId);
        });

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
            
            emitterRepository.deleteAllEventCacheStartWithMemberId(String.valueOf(memberId));
        }

        return emitter;
    }

    private void sendToClient(SseEmitter emitter, String eventId, String emitterId, Object data) {
        try {
            System.out.println("sendToClient: " + eventId);
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

    public void send(Member receiver, Member giver, TYPE type, Long targetId, String targetTitle, String message, Long commentId) {
        Notification notification = notificationRepository.findByReceiverAndGiverAndTypeAndTargetIdAndCommentId(receiver, giver, type, targetId, commentId)
                                        .orElseGet(() -> {
                                            return notificationRepository.save(new Notification(receiver, giver, type, targetId, message, commentId));
                                        });
        
        String receiverId = String.valueOf(receiver.getId());
        String eventId = receiverId + "_" + System.currentTimeMillis();

        Map<String, SseEmitter> sseEmitters = emitterRepository.findAllEmitterStartWithByMemberId(receiverId);
        sseEmitters.forEach(
            (key, emitter) -> {
                NotificationResponseDto notificationResponseDto = NotificationResponseDto.of(notification, targetTitle);
                emitterRepository.saveEventCache(eventId, notificationResponseDto);
                sendToClient(emitter, eventId, key, notificationResponseDto);
            }
        );
    }

    public Page<NotificationResponseDto> getAlarms(TYPE type, int pageNumber) {
        Member member = isMemberCurrent(memberRepository);
        return notificationRepository.findAllByMember(PageRequest.of(pageNumber - 1, 20), member, type);
    }

    /*
     * 전체 삭제 or 부분 삭제
     */
    public Long deleteAlarms(DeleteAlarmRequestDto requestDto) {
        Member member = isMemberCurrent(memberRepository);
        if(requestDto.getAll()) {
            System.out.println("isAll true");
            return notificationRepository.deleteAllAlarms(member);
        } else {
            if(requestDto.getDeleteList().size() == 0) {
                return 0L;
            } else {
                return notificationRepository.deleteSelectedAlarms(member, requestDto.getDeleteList());
            }
        }
    }

    /*
     * 모두 읽음 표시
     */
    public Long ChangeReadAlarms() {
        Member member = isMemberCurrent(memberRepository);
        return notificationRepository.readAllAlarms(member);        
    }

    /*
     * 타겟 포스트 (or 골) 찾기
     */
    public TargetResponseDto findTarget(TargetRequestDto requestDto) {
        return postContentRepository.findTarget(requestDto.getType(), requestDto.getTargetId(), requestDto.getCommentId());
    }

}
