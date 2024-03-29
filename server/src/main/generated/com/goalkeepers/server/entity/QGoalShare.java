package com.goalkeepers.server.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QGoalShare is a Querydsl query type for GoalShare
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QGoalShare extends EntityPathBase<GoalShare> {

    private static final long serialVersionUID = 1718612191L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QGoalShare goalShare = new QGoalShare("goalShare");

    public final QGoal goal;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final QMember member;

    public QGoalShare(String variable) {
        this(GoalShare.class, forVariable(variable), INITS);
    }

    public QGoalShare(Path<? extends GoalShare> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QGoalShare(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QGoalShare(PathMetadata metadata, PathInits inits) {
        this(GoalShare.class, metadata, inits);
    }

    public QGoalShare(Class<? extends GoalShare> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.goal = inits.isInitialized("goal") ? new QGoal(forProperty("goal"), inits.get("goal")) : null;
        this.member = inits.isInitialized("member") ? new QMember(forProperty("member"), inits.get("member")) : null;
    }

}

