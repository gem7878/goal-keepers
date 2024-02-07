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
    private Long receiverId;
    private Long giverId;
    private TYPE type;
    private Long targetId;
    private String targetTitle;
    private Long commentId;
    

    public static NotificationResponseDto of(Notification notification, String title, Long commentId) {
        return NotificationResponseDto.builder()
                    .notificationId(notification.getId())
                    .receiverId(notification.getReceiver().getId())
                    .giverId(notification.getGiver().getId())
                    .type(notification.getType())
                    .targetId(notification.getTargetId())
                    .targetTitle(title)
                    .commentId(commentId)
                    .build();
    }
}
