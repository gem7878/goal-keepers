package com.goalkeepers.server.entity;

import jakarta.persistence.CascadeType;
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
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name= "SETTING_TB")
public class Setting {
    @Id 
    @Column(name = "setting_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(mappedBy = "setting", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "member_id")
    @NotNull
    private Member member;

    @Column(columnDefinition = "boolean default true")
    private boolean commentAlarm;

    @Column(columnDefinition = "boolean default true")
    private boolean contentLikeAlarm;

    @Column(columnDefinition = "boolean default true")
    private boolean postCheerAlarm;

    @Column(columnDefinition = "boolean default true")
    private boolean goalShareAlarm;

    // @Column(columnDefinition = "boolean default true")
    // private boolean ddayAlarm;

    // @Column(columnDefinition = "boolean default true")
    // private boolean todayAlarm;

}
