package com.goalkeepers.server.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.goalkeepers.server.entity.Member;
import com.goalkeepers.server.entity.Post;
import com.goalkeepers.server.entity.PostComment;

@Repository
public interface CommentRepository extends JpaRepository<PostComment, Long>{
    List<PostComment> findAllByPost(Post post);
    Optional<PostComment> findByIdAndMember(Long id, Member member);
}
