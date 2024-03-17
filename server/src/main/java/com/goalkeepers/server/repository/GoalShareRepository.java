package com.goalkeepers.server.repository;

import java.util.Optional;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.goalkeepers.server.entity.Goal;
import com.goalkeepers.server.entity.GoalShare;
import com.goalkeepers.server.entity.Member;

@Repository
public interface GoalShareRepository extends JpaRepository<GoalShare, Long>{
    Boolean existsByMemberAndGoal(Member member, Goal goal);
    Boolean existsByGoal(Goal goal);
    Optional<GoalShare> findByMemberAndGoal(Member member, Goal goal);
    void deleteByMemberAndGoal(Member member, Goal goal);
    Set<GoalShare> findAllByMember(Member member);
    Set<GoalShare> findAllByGoal(Goal goal);
}
