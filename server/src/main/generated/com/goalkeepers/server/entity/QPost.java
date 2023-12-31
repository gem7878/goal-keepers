package com.goalkeepers.server.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QPost is a Querydsl query type for Post
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QPost extends EntityPathBase<Post> {

    private static final long serialVersionUID = 203969645L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QPost post = new QPost("post");

    public final ListPath<PostComment, QPostComment> comments = this.<PostComment, QPostComment>createList("comments", PostComment.class, QPostComment.class, PathInits.DIRECT2);

    public final StringPath content = createString("content");

    public final QGoal goal;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final NumberPath<Integer> likeCnt = createNumber("likeCnt", Integer.class);

    public final SetPath<PostLike, QPostLike> likes = this.<PostLike, QPostLike>createSet("likes", PostLike.class, QPostLike.class, PathInits.DIRECT2);

    public final QMember member;

    public final StringPath title = createString("title");

    public final DateTimePath<java.time.LocalDateTime> updatedAt = createDateTime("updatedAt", java.time.LocalDateTime.class);

    public QPost(String variable) {
        this(Post.class, forVariable(variable), INITS);
    }

    public QPost(Path<? extends Post> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QPost(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QPost(PathMetadata metadata, PathInits inits) {
        this(Post.class, metadata, inits);
    }

    public QPost(Class<? extends Post> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.goal = inits.isInitialized("goal") ? new QGoal(forProperty("goal"), inits.get("goal")) : null;
        this.member = inits.isInitialized("member") ? new QMember(forProperty("member")) : null;
    }

}

