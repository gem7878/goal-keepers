package com.goalkeepers.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.*;
import com.goalkeepers.server.model.User;

public interface UserRepository extends JpaRepository<User, Long>{
    Optional<User> findByEmail(String email);
}
