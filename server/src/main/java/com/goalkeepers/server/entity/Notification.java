package com.goalkeepers.server.entity;

import com.google.auto.value.AutoValue.Builder;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "NOTIFICATION_TB")
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "notification_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "receiver_id")
    private Member receiver;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "giver_id")
    private Member giver;

    private TYPE type;

    private Long targetId;

    private Long commentId;

    @Column(columnDefinition = "boolean default false")
    private Boolean isRead;

    public Notification(Member receiver, Member giver, TYPE type, Long targetId, Long commentId) {
        this.receiver = receiver;
        this.giver = giver;
        this.type = type;
        this.targetId = targetId;
        this.commentId = commentId;
    }
}
