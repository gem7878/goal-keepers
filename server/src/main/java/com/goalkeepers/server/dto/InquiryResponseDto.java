package com.goalkeepers.server.dto;

import java.time.LocalDateTime;

import com.goalkeepers.server.entity.Inquiry;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class InquiryResponseDto {

    private Long inquiryId;
    private String title;
    private String content;
    private LocalDateTime createdAt;
    private boolean isAnswered;

    public static InquiryResponseDto of(Inquiry inquiry) {
        return InquiryResponseDto.builder()
                .inquiryId(inquiry.getId())
                .title(inquiry.getTitle())
                .content(inquiry.getContent())
                .createdAt(inquiry.getCreatedAt())
                .isAnswered(inquiry.isAnswered())
                .build();
    }
}
