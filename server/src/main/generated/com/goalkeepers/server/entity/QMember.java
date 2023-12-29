package com.goalkeepers.server.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QMember is a Querydsl query type for Member
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QMember extends EntityPathBase<Member> {

    private static final long serialVersionUID = -1648982233L;

    public static final QMember member = new QMember("member1");

    public final StringPath email = createString("email");

    public final ListPath<Goal, QGoal> goals = this.<Goal, QGoal>createList("goals", Goal.class, QGoal.class, PathInits.DIRECT2);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final SetPath<PostLike, QPostLike> likes = this.<PostLike, QPostLike>createSet("likes", PostLike.class, QPostLike.class, PathInits.DIRECT2);

    public final StringPath nickname = createString("nickname");

    public final StringPath password = createString("password");

    public final ListPath<Post, QPost> posts = this.<Post, QPost>createList("posts", Post.class, QPost.class, PathInits.DIRECT2);

    public final EnumPath<Role> role = createEnum("role", Role.class);

    public final SetPath<GoalShare, QGoalShare> shares = this.<GoalShare, QGoalShare>createSet("shares", GoalShare.class, QGoalShare.class, PathInits.DIRECT2);

    public final EnumPath<SNS> sns = createEnum("sns", SNS.class);

    public final NumberPath<Long> snsId = createNumber("snsId", Long.class);

    public QMember(String variable) {
        super(Member.class, forVariable(variable));
    }

    public QMember(Path<? extends Member> path) {
        super(path.getType(), path.getMetadata());
    }

    public QMember(PathMetadata metadata) {
        super(Member.class, metadata);
    }

}

