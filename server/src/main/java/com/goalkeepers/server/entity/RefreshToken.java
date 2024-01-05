/*package com.goalkeepers.server.entity;

import java.time.LocalDateTime;

import jakarta.persistence.*;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name= "REFRESH_TOKEN_TB")
public class RefreshToken {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String refreshToken;

    @Column(columnDefinition = "timestamp without time zone")
    private LocalDateTime expiredDate;

    @Column(columnDefinition = "timestamp without time zone")
    private LocalDateTime createdDate;

    @OneToOne
    @JoinColumn(name = "MEMBER_ID")
    private Member member;
}*/
