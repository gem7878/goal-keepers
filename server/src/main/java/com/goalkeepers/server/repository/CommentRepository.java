package com.goalkeepers.server.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.goalkeepers.server.entity.Post;
import com.goalkeepers.server.entity.PostComment;

@Repository
public interface CommentRepository extends JpaRepository<PostComment, Long>{
    List<PostComment> findAllByPost(Post post);
}
