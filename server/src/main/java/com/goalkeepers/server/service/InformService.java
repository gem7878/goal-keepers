package com.goalkeepers.server.service;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import com.goalkeepers.server.common.ServiceHelper;
import com.goalkeepers.server.dto.InformRequestDto;
import com.goalkeepers.server.entity.Inform;
import com.goalkeepers.server.entity.Member;
import com.goalkeepers.server.entity.TYPE;
import com.goalkeepers.server.repository.InformRepository;
import com.goalkeepers.server.repository.MemberRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class InformService extends ServiceHelper {

    private final InformRepository informRepository;
    private final MemberRepository memberRepository;
    private final NotificationService notificationService;

    public Long createInform(InformRequestDto requestDto) {
        Inform inform = informRepository.save(new Inform(requestDto));
        Long id = inform.getId();
        // 알림 보내기
        for (Member receiver : memberRepository.findAll()) {
            notificationService.send(receiver, null, TYPE.NOTIFY, id, requestDto.getTitle(), requestDto.getContent(), null);
        }
        return id;
    }

    public void deleteInform(Long id) {
        informRepository.delete(informRepository.findById(id)
                                                .orElseThrow(() -> new EmptyResultDataAccessException(1)));
    }

}
