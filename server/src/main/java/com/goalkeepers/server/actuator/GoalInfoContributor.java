package com.goalkeepers.server.actuator;

import java.util.Map;
import java.util.HashMap;
import org.springframework.boot.actuate.info.Info.Builder;
import org.springframework.stereotype.Component;

import com.goalkeepers.server.repository.GoalRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.boot.actuate.info.InfoContributor;

@RequiredArgsConstructor
@Component
public class GoalInfoContributor implements InfoContributor{

    private final GoalRepository goalRepository;
    @Override
    public void contribute(Builder builder) {
        Long count = goalRepository.count();
        Map<String, Object> mapper = new HashMap<String, Object>();
        mapper.put("총 목표 개수", count);
        builder.withDetail("goal-stats", mapper);
    }

}
