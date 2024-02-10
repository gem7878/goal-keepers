package com.goalkeepers.server.repository;

import static com.goalkeepers.server.entity.QGoal.goal;
import static com.goalkeepers.server.entity.QNotification.notification;
import static com.goalkeepers.server.entity.QPost.post;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import com.goalkeepers.server.dto.NotificationResponseDto;
import com.goalkeepers.server.entity.Member;
import com.goalkeepers.server.entity.Notification;
import com.goalkeepers.server.entity.TYPE;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.querydsl.jpa.impl.JPAUpdateClause;
import com.querydsl.jpa.impl.JPADeleteClause;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;

@Transactional
@RequiredArgsConstructor
@Repository
public class NotificationRepositoryImpl implements NotificationRepositoryCustom {
    
    private final EntityManager entityManager;
    private final JPAQueryFactory queryFactory;

    @Override
    public Page<NotificationResponseDto> findAllByMember(Pageable pageable, Member member, TYPE type) {
        
        List<Notification> notifications = queryFactory
                                            .selectFrom(notification)
                                            .where(notification.receiver.eq(member)
                                                .and(type.equals(TYPE.ALL) ? null : type.equals(TYPE.ALARM) ? notification.type.in(TYPE.DAYLEFT, TYPE.DDAY, TYPE.WEEKLEFT) : notification.type.eq(type)))
                                            .orderBy(notification.id.desc())
                                            .offset(pageable.getOffset())
                                            .limit(pageable.getPageSize())
                                            .fetch();

        List<NotificationResponseDto> page = notifications
            .stream()
            .map(notification -> {
                String title = null;
                switch (notification.getType()) {
                    case WEEKLEFT:
                    case DAYLEFT:
                    case DDAY:
                    case SHARE:
                        title = queryFactory
                                .select(goal.title)
                                .from(goal)
                                .where(goal.id.eq(notification.getTargetId()))
                                .fetchOne();
                        break;
                    case LIKE:
                    case COMMENT:
                    case CHEER:
                        title = queryFactory
                                .select(goal.title)
                                .from(post)
                                .leftJoin(post.goal, goal)
                                .where(post.id.eq(notification.getTargetId()))
                                .fetchOne();
                        break;
                
                    case TODAY:
                        break;
                    default:
                        break;
                }
                return NotificationResponseDto.of(notification, title);
            }).collect(Collectors.toList());
        
        int totalSize = queryFactory
                        .selectFrom(notification)
                        .where(notification.receiver.eq(member))
                        .fetch()
                        .size();
        
        return new PageImpl<>(page, pageable, totalSize);
    }

    @Override
    public Long readAllAlarms(Member member) {
        return new JPAUpdateClause(entityManager, notification)
                    .where(notification.receiver.eq(member)
                            .and(notification.isRead.eq(false)))
                    .set(notification.isRead, true)
                    .execute();
    }

    @Override
    public Long deleteAllAlarms(Member member) {
        return new JPADeleteClause(entityManager, notification)
                    .where(notification.receiver.eq(member))
                    .execute();
    }

    @Override
    public Long deleteSelectedAlarms(Member member, List<Long> idList) {
        return new JPADeleteClause(entityManager, notification)
                    .where(notification.receiver.eq(member)
                        .and(notification.id.in(idList)))
                    .execute();
    }
}
