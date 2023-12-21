package com.goalkeepers.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.goalkeepers.server.entity.Goal;
import com.goalkeepers.server.entity.GoalShare;
import com.goalkeepers.server.entity.Member;
import com.goalkeepers.server.entity.Post;

@Repository
public interface GoalShareRepository extends JpaRepository<GoalShare, Long>{
    Boolean existsByMemberAndGoal(Member member, Goal goal);
    void deleteByMemberAndGoal(Member member, Goal goal);
}
