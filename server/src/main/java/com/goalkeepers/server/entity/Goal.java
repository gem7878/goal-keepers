package com.goalkeepers.server.entity;

import java.time.LocalDate;
import java.util.List;

import org.hibernate.annotations.ColumnDefault;

import com.goalkeepers.server.dto.GoalRequestDto;

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
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name= "GOAL_TB")
public class Goal {

    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "goal_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    @NotNull
    private Member member;

    @Column(length = 50)
    @NotNull
    private String title;

    @Column(columnDefinition = "TEXT")
    @NotNull
    private String description;

    @Column
    @NotNull
    private LocalDate startDate;

    @Column
    @NotNull
    private LocalDate endDate;

    @Column
    private String imageUrl;

    @ColumnDefault("0")
    private int shareCnt;

    @OneToMany(mappedBy = "goal")
    private List<Post> posts;

    public static Goal goalUpdate(Goal goal, GoalRequestDto requestDto) {
        goal.title = requestDto.getTitle();
        goal.description = requestDto.getDescription();
        goal.startDate = requestDto.getStartDate();
        goal.endDate = requestDto.getEndDate();
        goal.imageUrl = requestDto.getImageUrl();
        return goal;
    }
}
