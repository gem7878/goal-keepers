package com.goalkeepers.server.repository;

import java.util.Optional;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.goalkeepers.server.entity.Member;
import com.goalkeepers.server.entity.Post;
import com.goalkeepers.server.entity.PostCheer;

@Repository
public interface PostCheerRepository extends JpaRepository<PostCheer, Long>{
    Optional<PostCheer> findByMemberAndPost(Member member, Post post);
    boolean existsByMemberAndPost(Member member, Post post);
    List<PostCheer> findAllByMember(Member member);
}
