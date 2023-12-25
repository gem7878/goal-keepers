package com.goalkeepers.server.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.goalkeepers.server.entity.Member;
import com.goalkeepers.server.entity.Post;
import com.goalkeepers.server.entity.PostLike;

@Repository
public interface PostLikeRepository extends JpaRepository<PostLike, Long>{
    boolean existsByMemberAndPost(Member member, Post post);
    void deleteByMemberAndPost(Member memeber, Post post);
    List<PostLike> findAllByPost(Post post);
}
