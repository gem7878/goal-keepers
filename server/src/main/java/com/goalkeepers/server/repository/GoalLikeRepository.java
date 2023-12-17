package com.goalkeepers.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.goalkeepers.server.entity.GoalLike;

@Repository
public interface GoalLikeRepository extends JpaRepository<GoalLike, Long>{
    
}
