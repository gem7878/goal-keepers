package com.goalkeepers.server.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import com.goalkeepers.server.dto.InformRequestDto;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "INFORM_TB")
public class Inform {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "inform_id")
    private Long id;

    @Column(length = 50, nullable = false)
    private String title;

    @Column(length = 60, nullable = false)
    private String content;

    @CreationTimestamp
    private LocalDateTime createdAt;

    public Inform (InformRequestDto requestDto) {
        this.title = requestDto.getTitle();
        this.content = requestDto.getContent();
    }
}
