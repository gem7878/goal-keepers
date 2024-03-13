package com.goalkeepers.server.repository;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.goalkeepers.server.dto.InquiryResponseDto;
import com.goalkeepers.server.entity.Inquiry;
import com.goalkeepers.server.entity.Member;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

import static com.goalkeepers.server.entity.QInquiry.inquiry;


@RequiredArgsConstructor
@Repository
public class InquiryRepositoryImpl implements InquiryRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<InquiryResponseDto> findAllByPage(Pageable pageable, Member member) {
        List<Inquiry> myInquiries = queryFactory
                                    .selectFrom(inquiry)
                                    .where(inquiry.member.eq(member))
                                    .orderBy(inquiry.createdAt.desc())
                                    .offset(pageable.getOffset())
                                    .limit(pageable.getPageSize())
                                    .fetch();

        List<InquiryResponseDto> page = myInquiries
                                        .stream()
                                        .map(InquiryResponseDto::of)
                                        .collect(Collectors.toList());

        int totalSize = queryFactory
                        .selectFrom(inquiry)
                        .where(inquiry.member.eq(member))
                        .fetch()
                        .size();
                                
        return new PageImpl<>(page, pageable, totalSize);
    }

}
