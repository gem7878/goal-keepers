package com.goalkeepers.server.repository;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.goalkeepers.server.dto.FAQResponseDto;
import com.goalkeepers.server.entity.Faq;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

import static com.goalkeepers.server.entity.QFaq.faq;

@RequiredArgsConstructor
@Repository
public class FaqRepositoryImpl implements FaqRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<FAQResponseDto> findAllByPage(Pageable pageable) {

        List<Faq> faqs = queryFactory
                        .selectFrom(faq)
                        .orderBy(faq.id.asc())
                        .offset(pageable.getOffset())
                        .limit(pageable.getPageSize())
                        .fetch();

        List<FAQResponseDto> page = faqs
                                    .stream()
                                    .map(FAQResponseDto::of)
                                    .collect(Collectors.toList());

        int totalSize = queryFactory
                        .selectFrom(faq)
                        .fetch()
                        .size();
                        
        return new PageImpl<>(page, pageable, totalSize);
    }

}
