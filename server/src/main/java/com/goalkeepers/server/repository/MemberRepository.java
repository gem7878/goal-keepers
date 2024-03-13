package com.goalkeepers.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.goalkeepers.server.entity.Member;
import com.goalkeepers.server.entity.Role;

import java.util.Optional;
import java.util.List;


@Repository
public interface MemberRepository extends JpaRepository<Member, Long>{
    Optional<Member> findByEmail(String email);
    boolean existsByEmail(String email);
    boolean existsByNickname(String nickname);
    Optional<Member> findBySnsId(Long snsId);
    List<Member> findAllByRole(Role role);
}
