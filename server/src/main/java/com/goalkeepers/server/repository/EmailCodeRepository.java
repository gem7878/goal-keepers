package com.goalkeepers.server.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.goalkeepers.server.entity.EmailCode;

@Repository
public interface EmailCodeRepository extends JpaRepository<EmailCode, Long> {
    Optional<EmailCode> findByEmail(String email);
    Boolean existsByEmail(String email);
    void deleteByEmail(String email);
}