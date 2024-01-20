package com.goalkeepers.server.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.goalkeepers.server.entity.Member;
import com.goalkeepers.server.entity.PostContent;
import com.goalkeepers.server.entity.PostLike;

@Repository
public interface PostLikeRepository extends JpaRepository<PostLike, Long>{
    boolean existsByMemberAndPostContent(Member member, PostContent content);
    void deleteByMemberAndPostContent(Member member, PostContent content);
    List<PostLike> findAllByPostContent(PostContent content);
}
