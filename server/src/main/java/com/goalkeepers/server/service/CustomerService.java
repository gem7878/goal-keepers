package com.goalkeepers.server.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.goalkeepers.server.common.ServiceHelper;
import com.goalkeepers.server.dto.AnswerRequestDto;
import com.goalkeepers.server.dto.AnswerResponseDto;
import com.goalkeepers.server.dto.FAQRequestDto;
import com.goalkeepers.server.dto.FAQResponseDto;
import com.goalkeepers.server.dto.FAQUpdateRequestDto;
import com.goalkeepers.server.dto.InquiryRequestDto;
import com.goalkeepers.server.dto.InquiryResponseDto;
import com.goalkeepers.server.entity.Answer;
import com.goalkeepers.server.entity.Faq;
import com.goalkeepers.server.entity.Inquiry;
import com.goalkeepers.server.entity.Member;
import com.goalkeepers.server.exception.CustomException;
import com.goalkeepers.server.repository.AnswerRepository;
import com.goalkeepers.server.repository.FaqRepository;
import com.goalkeepers.server.repository.InquiryRepository;
import com.goalkeepers.server.repository.MemberRepository;

import lombok.RequiredArgsConstructor;

@Transactional
@RequiredArgsConstructor
@Service
public class CustomerService extends ServiceHelper {

    private final FaqRepository faqRepository;
    private final InquiryRepository inquiryRepository;
    private final AnswerRepository answerRepository;
    private final MemberRepository memberRepository;

    public Long createFaq(FAQRequestDto requestDto) {
        Faq newFaq = faqRepository.save(new Faq(requestDto.getTitle(), requestDto.getContent()));
        return newFaq.getId();
    }

    public void updateFaq(Long faqId, FAQUpdateRequestDto requestDto) {
        Faq.updateFaq(findOneFaq(faqId), requestDto.getTitle(), requestDto.getContent());
    }

    public void deleteFaq(Long faqId) {
        faqRepository.delete(findOneFaq(faqId));    
    }

    public Long createAnswer(AnswerRequestDto requestDto) {
        Inquiry inquiry = findOneInquiry(requestDto.getInquiryId());
        Answer newAnswer = answerRepository.save(requestDto.toAnswer(inquiry, isMemberCurrent(memberRepository)));
        inquiry.setAnswered(true);
        return newAnswer.getId();
    }

    public Page<FAQResponseDto> getAllFaq(int pageNumber) {
        return faqRepository.findAllByPage(PageRequest.of(pageNumber - 1, 10));
    }

    public Page<InquiryResponseDto> getMyInquiry(int pageNumber) {
        return inquiryRepository.findAllByPage(PageRequest.of(pageNumber - 1, 10), isMemberCurrent(memberRepository));
    }

    public Long createInquiry(InquiryRequestDto requestDto) {
        Member member = isMemberCurrent(memberRepository);
        Inquiry newInquiry = inquiryRepository.save(new Inquiry(member, requestDto.getTitle(), requestDto.getContent()));
        return newInquiry.getId();
    }

    public AnswerResponseDto getOneAnswer(Long inquiryId) {
        return AnswerResponseDto.of(findOneAnswer(inquiryId));
    }

    private Faq findOneFaq(Long faqId) {
        return faqRepository.findById(faqId).orElseThrow(() -> new CustomException(faqId + " FAQ 정보가 없습니다."));
    }

    private Inquiry findOneInquiry(Long inquiryId) {
        return inquiryRepository.findById(inquiryId).orElseThrow(() -> new CustomException(inquiryId + " 문의 정보가 없습니다."));
    }

    private Answer findOneAnswer(Long inquiryId) {
        return answerRepository.findByInquiry(findOneInquiry(inquiryId))
                                .orElseThrow(() -> new CustomException("답변 정보가 없습니다."));
    }
}
