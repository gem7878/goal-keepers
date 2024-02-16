package com.goalkeepers.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.goalkeepers.server.entity.Faq;

@Repository
public interface FaqRepository extends JpaRepository<Faq, Long>, FaqRepositoryCustom{

}
