package com.goalkeepers.server.entity;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import org.hibernate.annotations.ColumnDefault;

import com.goalkeepers.server.dto.GoalRequestDto;
import com.goalkeepers.server.dto.GoalUpdateRequestDto;

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
import jakarta.persistence.OneToOne;
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
    private String description;

    @Column
    private LocalDate startDate;

    @Column
    private LocalDate endDate;

    @Column
    private String imageUrl;

    @ColumnDefault("0")
    private int shareCnt;

    @OneToMany(mappedBy = "goal", fetch = FetchType.LAZY)
    private Set<GoalShare> shareList;

    @OneToOne
    @JoinColumn(name = "share_id", nullable = true)
    private GoalShare share;

    @OneToMany(mappedBy = "goal")
    private List<Post> posts;

    public static Goal goalUpdate(Goal goal, GoalUpdateRequestDto requestDto, String imageUrl) {
        goal.title = requestDto.getTitle();
        goal.description = requestDto.getDescription();
        goal.startDate = requestDto.getStartDate();
        goal.endDate = requestDto.getEndDate();
        goal.imageUrl = imageUrl;
        return goal;
    }

    public static Goal goalUpdate(Goal goal, GoalUpdateRequestDto requestDto) {
        goal.title = requestDto.getTitle();
        goal.description = requestDto.getDescription();
        goal.startDate = requestDto.getStartDate();
        goal.endDate = requestDto.getEndDate();
        return goal;
    }

    public static Goal goalUpdate(Goal goal, String imageUrl) {
        goal.imageUrl = imageUrl;
        return goal;
    }

    public Goal(GoalShare share, String title, String description, String imageUrl, LocalDate startDate, LocalDate endDate, Member member) {
        this.share = share;
        this.member = member;
        this.title = title;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
        this.imageUrl = imageUrl;
    }
}
