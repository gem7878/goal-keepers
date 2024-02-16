package com.goalkeepers.server.controller;

import org.springframework.web.bind.annotation.RestController;

import com.goalkeepers.server.dto.AnswerRequestDto;
import com.goalkeepers.server.dto.AnswerResponseDto;
import com.goalkeepers.server.dto.CommonResponseDto;
import com.goalkeepers.server.dto.FAQRequestDto;
import com.goalkeepers.server.dto.FAQResponseDto;
import com.goalkeepers.server.dto.FAQUpdateRequestDto;
import com.goalkeepers.server.dto.InquiryRequestDto;
import com.goalkeepers.server.dto.InquiryResponseDto;
import com.goalkeepers.server.service.CustomerService;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;

    /*
     * FAQ 생성
     * FAQ 수정
     * FAQ 삭제
     */
    @PostMapping("/admin/faq")
    public ResponseEntity<CommonResponseDto> createFaq(@RequestBody FAQRequestDto requestDto) {
        Long faqId = customerService.createFaq(requestDto);
        return ResponseEntity.ok(new CommonResponseDto(true, faqId + " FAQ가 생성되었습니다."));
    }

    @PutMapping("/admin/faq")
    public ResponseEntity<CommonResponseDto> updateFaq(@RequestParam(name = "faq-id") Long faqId,
                                                        @RequestBody FAQUpdateRequestDto requestDto) {
        customerService.updateFaq(faqId, requestDto);
        return ResponseEntity.ok(new CommonResponseDto(true, faqId + " FAQ가 수정되었습니다."));
    }

    @DeleteMapping("/admin/faq")
    public ResponseEntity<CommonResponseDto> deleteFaq(@RequestParam(name = "faq-id") Long faqId) {
        customerService.deleteFaq(faqId);
        return ResponseEntity.ok(new CommonResponseDto(true, faqId + " FAQ가 삭제되었습니다."));
    }  


    /*
     * 문의 답변하기
     */
    @PostMapping("/admin/answer")
    public ResponseEntity<CommonResponseDto> createAnswer(@RequestBody AnswerRequestDto requestDto) {
        Long answerId = customerService.createAnswer(requestDto);
        return ResponseEntity.ok(new CommonResponseDto(true, answerId + " 답변이 생성되었습니다."));
    }


    /*
     * FAQ 보기
     */
    @GetMapping("/faq")
    public ResponseEntity<CommonResponseDto> getAllFaq(@RequestParam(name = "page") int pageNumber) {
        Page<FAQResponseDto> response = customerService.getAllFaq(pageNumber);
        return ResponseEntity.ok(new CommonResponseDto(true, response));
    }


    /*
     * 나의 문의 보기
     * 나의 문의 보기 - 답변
     * 나의 문의 생성
     */
    @GetMapping("/inquiry/all")
    public ResponseEntity<CommonResponseDto> getMyInquiry(@RequestParam(name = "page") int pageNumber) {
        Page<InquiryResponseDto> response = customerService.getMyInquiry(pageNumber);
        return ResponseEntity.ok(new CommonResponseDto(true, response));
    }

    @GetMapping("/inquiry")
    public ResponseEntity<CommonResponseDto> getOneAnswer(@RequestParam(name = "inquiry-id") Long inquiryId) {
        AnswerResponseDto response = customerService.getOneAnswer(inquiryId);
        return ResponseEntity.ok(new CommonResponseDto(true, response));
    }

    @PostMapping("/inquiry")
    public ResponseEntity<CommonResponseDto> createInquiry(@RequestBody InquiryRequestDto requestDto) {
        Long inquiryId = customerService.createInquiry(requestDto);
        return ResponseEntity.ok(new CommonResponseDto(true, inquiryId + " 문의가 생성되었습니다."));
    }
}
