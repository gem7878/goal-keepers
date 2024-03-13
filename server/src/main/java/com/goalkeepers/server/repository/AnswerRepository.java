package com.goalkeepers.server.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.goalkeepers.server.entity.Answer;
import com.goalkeepers.server.entity.Inquiry;

@Repository
public interface AnswerRepository extends JpaRepository<Answer, Long> {
    Optional<Answer> findByInquiry(Inquiry inquiry);
}
