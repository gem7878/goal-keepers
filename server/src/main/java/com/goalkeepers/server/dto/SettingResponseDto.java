package com.goalkeepers.server.dto;

import com.goalkeepers.server.entity.Setting;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SettingResponseDto {
    private boolean commentAlarm;
    private boolean contentLikeAlarm;
    private boolean postCheerAlarm;
    private boolean goalShareAlarm;

    public static SettingResponseDto of(Setting setting) {
        return SettingResponseDto.builder()
                    .commentAlarm(setting.isCommentAlarm())
                    .contentLikeAlarm(setting.isContentLikeAlarm())
                    .postCheerAlarm(setting.isPostCheerAlarm())
                    .goalShareAlarm(setting.isGoalShareAlarm())
                    .build();
    }
}
