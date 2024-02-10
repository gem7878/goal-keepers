package com.goalkeepers.server.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class SettingUpdateRequestDto {
    private boolean commentAlarm;
    private boolean contentLikeAlarm;
    private boolean postCheerAlarm;
    private boolean goalShareAlarm;
    private boolean ddayAlarm;
    private boolean todayAlarm;
}
