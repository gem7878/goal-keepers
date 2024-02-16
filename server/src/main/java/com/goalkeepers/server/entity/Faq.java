package com.goalkeepers.server.entity;

import java.util.Optional;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "FAQ_TB")
public class Faq {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "faq_id")
    private Long id;

    @Column(length = 50)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String content;

    public Faq(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public static Faq updateFaq(Faq faq, String title, String content) {
        faq.title = Optional.ofNullable(title).orElse(faq.getTitle());
        faq.content = Optional.ofNullable(content).orElse(faq.getContent());
        return faq;
    }
}
