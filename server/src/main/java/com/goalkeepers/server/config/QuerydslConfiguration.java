package com.goalkeepers.server.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import com.querydsl.jpa.impl.JPAQueryFactory;


@Configuration
public class QuerydslConfiguration {
    
    @PersistenceContext
    private EntityManager entityManager;

    @Bean
    JPAQueryFactory JPAQueryFactory() {
        return new JPAQueryFactory(entityManager);
    }
}
