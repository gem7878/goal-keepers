package com.goalkeepers.server.entity;

import jakarta.persistence.CascadeType;
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
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "NOTIFICATION_TB")
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "notification_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "receiver_id", nullable = false)
    private Member receiver;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "giver_id")
    private Member giver;

    @Column(nullable = false)
    private TYPE type;

    private Long targetId;

    @Column(length = 60)
    private String message;

    private Long commentId;

    @Column(nullable = false, columnDefinition = "boolean default false")
    private Boolean isRead;

    public Notification(Member receiver, Member giver, TYPE type, Long targetId, String message, Long commentId) {
        this.receiver = receiver;
        this.giver = giver;
        this.type = type;
        this.targetId = targetId;
        this.message = message;
        this.commentId = commentId;
        this.isRead = false;
    }
}
