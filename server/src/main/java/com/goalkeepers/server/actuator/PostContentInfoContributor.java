package com.goalkeepers.server.actuator;

import java.util.Map;
import java.util.HashMap;
import org.springframework.boot.actuate.info.Info.Builder;
import org.springframework.stereotype.Component;

import com.goalkeepers.server.repository.PostContentRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.boot.actuate.info.InfoContributor;

@RequiredArgsConstructor
@Component
public class PostContentInfoContributor implements InfoContributor{

    private final PostContentRepository contentRepository;
    @Override
    public void contribute(Builder builder) {
        Long count = contentRepository.count();
        Map<String, Object> mapper = new HashMap<String, Object>();
        mapper.put("총 컨텐트 개수", count);
        builder.withDetail("content-stats", mapper);
    }

}
