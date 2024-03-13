package com.goalkeepers.server.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.goalkeepers.server.entity.Member;
import com.goalkeepers.server.entity.Notification;
import com.goalkeepers.server.entity.TYPE;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> , NotificationRepositoryCustom{
    Optional<Notification> findByReceiverAndGiverAndTypeAndTargetIdAndCommentId(Member receiver, Member giver, TYPE type, Long targetId, Long commentId);
}
