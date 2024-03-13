package com.goalkeepers.server.entity;

import java.util.Objects;

import com.goalkeepers.server.dto.SettingUpdateRequestDto;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.FetchType;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name= "SETTING_TB")
public class Setting {
    @Id 
    @Column(name = "setting_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    @NotNull
    private Member member;

    @Column(columnDefinition = "boolean not null default true")
    private boolean commentAlarm;

    @Column(columnDefinition = "boolean not null default true")
    private boolean contentLikeAlarm;

    @Column(columnDefinition = "boolean not null default true")
    private boolean postCheerAlarm;

    @Column(columnDefinition = "boolean not null default true")
    private boolean goalShareAlarm;

    @Column(columnDefinition = "boolean not null default true")
    private boolean ddayAlarm;

    @Column(columnDefinition = "boolean not null default true")
    private boolean todayAlarm;

    public Setting(Member member) {
        this.member = member;
        this.commentAlarm = true;
        this.contentLikeAlarm = true;
        this.postCheerAlarm = true;
        this.goalShareAlarm = true;
        this.ddayAlarm = true;
        this.todayAlarm = true;
    }

    public static Setting settingUpdate(Setting setting, SettingUpdateRequestDto requestDto) {
        if(Objects.isNull(requestDto)) {
            return setting;
        }
        if (setting.isCommentAlarm() != requestDto.isCommentAlarm()) {
            setting.commentAlarm = requestDto.isCommentAlarm();
        }
        if (setting.isContentLikeAlarm() != requestDto.isContentLikeAlarm()) {
            setting.contentLikeAlarm = requestDto.isContentLikeAlarm();
        }
        if (setting.isGoalShareAlarm() != requestDto.isGoalShareAlarm()) {
            setting.goalShareAlarm = requestDto.isGoalShareAlarm();
        }
        if (setting.isPostCheerAlarm() != requestDto.isPostCheerAlarm()) {
            setting.postCheerAlarm = requestDto.isPostCheerAlarm();
        }
        if (setting.isDdayAlarm() != requestDto.isDdayAlarm()) {
            setting.ddayAlarm = requestDto.isDdayAlarm();
        }
        if (setting.isTodayAlarm() != requestDto.isTodayAlarm()) {
            setting.todayAlarm = requestDto.isTodayAlarm();
        }
        return setting;
    }

}
