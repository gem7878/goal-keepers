package com.goalkeepers.server.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.goalkeepers.server.entity.Goal;
import com.goalkeepers.server.entity.Member;
import com.goalkeepers.server.entity.PostContent;

@Repository
public interface PostContentRepository extends JpaRepository<PostContent, Long>, PostContentRepositoryCustom {
    Optional<PostContent> findByIdAndMember(Long id, Member member);
    List<PostContent> findAllByShareGoal(Goal goal);
}
