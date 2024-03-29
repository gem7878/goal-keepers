package com.goalkeepers.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.goalkeepers.server.entity.Goal;
import com.goalkeepers.server.entity.GoalShare;
import com.goalkeepers.server.entity.Member;

import java.util.Optional;
import java.util.List;

@Repository
public interface GoalRepository extends JpaRepository<Goal, Long>, GoalRepositoryCustom {
    Optional<Goal> findById(Long id);
    Optional<Goal> findByShare(GoalShare share);
    Optional<Goal> findByIdAndMember(Long id, Member member);
    boolean existsByShare(GoalShare share);
    List<Goal> findAllByMember(Member member);
}