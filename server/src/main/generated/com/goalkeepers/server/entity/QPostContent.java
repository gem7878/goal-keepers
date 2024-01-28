package com.goalkeepers.server.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QPostContent is a Querydsl query type for PostContent
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QPostContent extends EntityPathBase<PostContent> {

    private static final long serialVersionUID = 811668300L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QPostContent postContent = new QPostContent("postContent");

    public final StringPath content = createString("content");

    public final DateTimePath<java.time.LocalDateTime> createdAt = createDateTime("createdAt", java.time.LocalDateTime.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final NumberPath<Integer> likeCnt = createNumber("likeCnt", Integer.class);

    public final SetPath<PostLike, QPostLike> likes = this.<PostLike, QPostLike>createSet("likes", PostLike.class, QPostLike.class, PathInits.DIRECT2);

    public final QMember member;

    public final QPost post;

    public final QGoal shareGoal;

    public final DateTimePath<java.time.LocalDateTime> updatedAt = createDateTime("updatedAt", java.time.LocalDateTime.class);

    public QPostContent(String variable) {
        this(PostContent.class, forVariable(variable), INITS);
    }

    public QPostContent(Path<? extends PostContent> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QPostContent(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QPostContent(PathMetadata metadata, PathInits inits) {
        this(PostContent.class, metadata, inits);
    }

    public QPostContent(Class<? extends PostContent> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.member = inits.isInitialized("member") ? new QMember(forProperty("member")) : null;
        this.post = inits.isInitialized("post") ? new QPost(forProperty("post"), inits.get("post")) : null;
        this.shareGoal = inits.isInitialized("shareGoal") ? new QGoal(forProperty("shareGoal"), inits.get("shareGoal")) : null;
    }

}

