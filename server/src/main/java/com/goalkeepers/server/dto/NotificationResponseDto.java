package com.goalkeepers.server.dto;

import com.goalkeepers.server.entity.Notification;
import com.goalkeepers.server.entity.TYPE;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NotificationResponseDto {
    private Long notificationId;
    private Long memberId;
    private TYPE type;
    private String message;
    private Long targetId;

    public static NotificationResponseDto of(Notification notification) {
        return NotificationResponseDto.builder()
                    .notificationId(notification.getId())
                    .memberId(notification.getReceiver().getId())
                    .type(notification.getType())
                    .message(notification.getMessage())
                    .targetId(notification.getTargetId())
                    .build();
    }
}
