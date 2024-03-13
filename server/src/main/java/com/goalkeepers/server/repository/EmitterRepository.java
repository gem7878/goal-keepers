package com.goalkeepers.server.repository;

import java.util.Map;

import org.springframework.stereotype.Repository;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Repository
public interface EmitterRepository {
    SseEmitter save(String emitterId, SseEmitter sseEmitter);
    void saveEventCache(String eventId, Object data);
    Map<String, SseEmitter> findAllEmitterStartWithByMemberId(String memberId);
    Map<String, Object> findAllEventCacheStartWithByMemberId(String memberId);
    void deleteById(String id);
    void deleteAllEmitterStartWithMemberId(String memberId);
    void deleteAllEventCacheStartWithMemberId(String memberId);
    Map<String, SseEmitter> findAllEmitters();
}
