package com.goalkeepers.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.goalkeepers.server.entity.Inform;

@Repository
public interface InformRepository extends JpaRepository<Inform, Long> {

}
