package com.goalkeepers.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.goalkeepers.server.entity.Notification;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {

}
