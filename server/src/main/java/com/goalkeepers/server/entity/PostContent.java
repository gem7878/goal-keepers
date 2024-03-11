package com.goalkeepers.server.entity;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;

import com.goalkeepers.server.dto.PostContentUpdateRequestDto;

import java.time.LocalDateTime;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Table(name = "CONTENT_TB")
public class PostContent {

    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "content_id")
    private Long id;

    @Column(length = 60)
    @NotNull
    private String content;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @ColumnDefault("0")
    private int likeCnt;

    @OneToMany(mappedBy = "postContent", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<PostLike> likes;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "post_id")
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    @NotNull
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "goal_id", nullable = true)
    private Goal shareGoal;

    public static PostContent postUpdate(PostContent postContent, PostContentUpdateRequestDto requestDto) {
        postContent.content = requestDto.getContent();
        return postContent;
    }
}
