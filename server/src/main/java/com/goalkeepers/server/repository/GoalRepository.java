package com.goalkeepers.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.goalkeepers.server.entity.Goal;
import com.goalkeepers.server.entity.GoalShare;
import com.goalkeepers.server.entity.Member;

import java.util.List;
import java.util.Optional;

@Repository
public interface GoalRepository extends JpaRepository<Goal, Long>{
    List<Goal> findAllByMember(Member member);
    Optional<Goal> findById(Long id);
    Optional<Goal> findByShare(GoalShare share);
    Optional<Goal> findByIdAndMember(Long id, Member member);
}