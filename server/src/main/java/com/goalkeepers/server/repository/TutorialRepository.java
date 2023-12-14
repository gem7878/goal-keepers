package com.goalkeepers.server.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.goalkeepers.server.entity.Tutorial;

public interface TutorialRepository extends JpaRepository<Tutorial, Long>{
    
    List<Tutorial> findByPublished(boolean published);
    List<Tutorial> findByTitleContaining(String title);
}
