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

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QMember member = new QMember("member1");

    public final SetPath<PostCheer, QPostCheer> cheers = this.<PostCheer, QPostCheer>createSet("cheers", PostCheer.class, QPostCheer.class, PathInits.DIRECT2);

    public final StringPath email = createString("email");

    public final ListPath<Goal, QGoal> goals = this.<Goal, QGoal>createList("goals", Goal.class, QGoal.class, PathInits.DIRECT2);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final ListPath<Inquiry, QInquiry> inquiries = this.<Inquiry, QInquiry>createList("inquiries", Inquiry.class, QInquiry.class, PathInits.DIRECT2);

    public final SetPath<PostLike, QPostLike> likes = this.<PostLike, QPostLike>createSet("likes", PostLike.class, QPostLike.class, PathInits.DIRECT2);

    public final StringPath nickname = createString("nickname");

    public final ListPath<Notification, QNotification> notifications = this.<Notification, QNotification>createList("notifications", Notification.class, QNotification.class, PathInits.DIRECT2);

    public final StringPath password = createString("password");

    public final ListPath<PostContent, QPostContent> postContents = this.<PostContent, QPostContent>createList("postContents", PostContent.class, QPostContent.class, PathInits.DIRECT2);

    public final ListPath<Post, QPost> posts = this.<Post, QPost>createList("posts", Post.class, QPost.class, PathInits.DIRECT2);

    public final EnumPath<Role> role = createEnum("role", Role.class);

    public final QSetting setting;

    public final SetPath<GoalShare, QGoalShare> shares = this.<GoalShare, QGoalShare>createSet("shares", GoalShare.class, QGoalShare.class, PathInits.DIRECT2);

    public final EnumPath<SNS> sns = createEnum("sns", SNS.class);

    public final NumberPath<Long> snsId = createNumber("snsId", Long.class);

    public QMember(String variable) {
        this(Member.class, forVariable(variable), INITS);
    }

    public QMember(Path<? extends Member> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QMember(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QMember(PathMetadata metadata, PathInits inits) {
        this(Member.class, metadata, inits);
    }

    public QMember(Class<? extends Member> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.setting = inits.isInitialized("setting") ? new QSetting(forProperty("setting"), inits.get("setting")) : null;
    }

}

