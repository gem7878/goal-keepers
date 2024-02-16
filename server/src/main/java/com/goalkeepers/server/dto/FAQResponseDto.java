package com.goalkeepers.server.dto;

import com.goalkeepers.server.entity.Faq;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FAQResponseDto {
    private String title;
    private String content;

    public static FAQResponseDto of (Faq faq) {
        return FAQResponseDto.builder()
                .title(faq.getTitle())
                .content(faq.getContent())
                .build();
    }
}
