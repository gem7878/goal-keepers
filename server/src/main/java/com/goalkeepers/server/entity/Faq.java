package com.goalkeepers.server.entity;

import java.util.Optional;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "FAQ_TB")
public class Faq {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "faq_id")
    private Long id;

    @Column(length = 50, nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT", nullable = false)
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
