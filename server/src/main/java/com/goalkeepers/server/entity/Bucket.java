package com.goalkeepers.server.entity;

//import java.time.LocalDateTime;
import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name= "BUCKET_TB")
public class Bucket {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MEMBER_ID")
    private Member member;

    @Column(length = 50)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String content;

    private LocalDate startDate;

    private LocalDate endDate;

    @Column(unique = true)
    private String imageUrl;
}
