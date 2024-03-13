package com.goalkeepers.server.dto;

import com.goalkeepers.server.entity.Notification;
import com.goalkeepers.server.entity.TYPE;

import java.util.Objects;

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
    private long notificationId;
    private long receiverId;
    private Long giverId;
    private String giverNickname;
    private TYPE type;
    private Long targetId;
    private String targetTitle;
    private String message;
    private Long commentId;
    

    public static NotificationResponseDto of(Notification notification, String title) {
        return NotificationResponseDto.builder()
                    .notificationId(notification.getId())
                    .receiverId(notification.getReceiver().getId())
                    .giverId(Objects.nonNull(notification.getGiver()) ? notification.getGiver().getId() : null)
                    .giverNickname(Objects.nonNull(notification.getGiver()) ? notification.getGiver().getNickname() : null)
                    .type(notification.getType())
                    .targetId(notification.getTargetId())
                    .targetTitle(title)
                    .message(notification.getMessage())
                    .commentId(notification.getCommentId())
                    .build();
    }
}
