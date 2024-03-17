package com.goalkeepers.server.actuator;

import java.util.Map;
import java.util.HashMap;
import org.springframework.boot.actuate.info.Info.Builder;
import org.springframework.stereotype.Component;

import com.goalkeepers.server.repository.PostRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.boot.actuate.info.InfoContributor;

@RequiredArgsConstructor
@Component
public class PostInfoContributor implements InfoContributor {

    private final PostRepository postRepository;

    @Override
    public void contribute(Builder builder) {
        Long postCount = postRepository.count();
        Map<String, Object> postMap = new HashMap<String, Object>();
        postMap.put("총 포스트 개수", postCount);
        builder.withDetail("post-stats", postMap);
    }

}
