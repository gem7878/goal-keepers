package com.goalkeepers.server.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QPostCheer is a Querydsl query type for PostCheer
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QPostCheer extends EntityPathBase<PostCheer> {

    private static final long serialVersionUID = 1640848608L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QPostCheer postCheer = new QPostCheer("postCheer");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final QMember member;

    public final QPost post;

    public QPostCheer(String variable) {
        this(PostCheer.class, forVariable(variable), INITS);
    }

    public QPostCheer(Path<? extends PostCheer> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QPostCheer(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QPostCheer(PathMetadata metadata, PathInits inits) {
        this(PostCheer.class, metadata, inits);
    }

    public QPostCheer(Class<? extends PostCheer> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.member = inits.isInitialized("member") ? new QMember(forProperty("member"), inits.get("member")) : null;
        this.post = inits.isInitialized("post") ? new QPost(forProperty("post"), inits.get("post")) : null;
    }

}

