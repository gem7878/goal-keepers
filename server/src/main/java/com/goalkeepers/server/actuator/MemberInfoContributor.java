package com.goalkeepers.server.actuator;

import java.util.Map;
import java.util.HashMap;
import org.springframework.boot.actuate.info.Info.Builder;
import org.springframework.stereotype.Component;

import com.goalkeepers.server.common.ServiceHelper;
import com.goalkeepers.server.entity.Member;
import com.goalkeepers.server.repository.MemberRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.boot.actuate.info.InfoContributor;

@RequiredArgsConstructor
@Component
public class MemberInfoContributor extends ServiceHelper implements InfoContributor{

    private final MemberRepository memberRepository;
    @Override
    public void contribute(Builder builder) {
        Member admin = isMemberCurrent(memberRepository);
        Map<String, Object> adminMapper = new HashMap<String, Object>();
        adminMapper.put("id", admin.getId());
        adminMapper.put("email", admin.getEmail());
        builder.withDetail("current-admin", adminMapper);
        Long count = memberRepository.count();
        Map<String, Object> mapper = new HashMap<String, Object>();
        mapper.put("총 유저 수", count);
        builder.withDetail("content-stats", mapper);
    }

}
