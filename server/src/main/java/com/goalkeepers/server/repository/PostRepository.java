package com.goalkeepers.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.goalkeepers.server.entity.Post;

@Repository
public interface PostRepository extends JpaRepository<Post, Long>{
    
}
