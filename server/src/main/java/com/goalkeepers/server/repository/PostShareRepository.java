package com.goalkeepers.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.goalkeepers.server.entity.PostShare;

@Repository
public interface PostShareRepository extends JpaRepository<PostShare, Long>{
    
}
