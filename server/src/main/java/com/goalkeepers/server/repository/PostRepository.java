package com.goalkeepers.server.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.goalkeepers.server.entity.Goal;
import com.goalkeepers.server.entity.Post;

@Repository
public interface PostRepository extends JpaRepository<Post, Long>{
    List<Post> findAllByGoal(Optional<Goal> goal);
    List<Post> findAllByOrderByUpdatedAtDesc();
}
