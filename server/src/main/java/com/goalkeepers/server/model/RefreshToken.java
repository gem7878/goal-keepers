package com.goalkeepers.server.model;

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
    private String user_id;

    @Column(unique = true)
    private String refresh_token;

    private String expired_date;

    private String created_date;
}
