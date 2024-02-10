package com.goalkeepers.server.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QInform is a Querydsl query type for Inform
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QInform extends EntityPathBase<Inform> {

    private static final long serialVersionUID = -1755382794L;

    public static final QInform inform = new QInform("inform");

    public final StringPath content = createString("content");

    public final DateTimePath<java.time.LocalDateTime> createdAt = createDateTime("createdAt", java.time.LocalDateTime.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public QInform(String variable) {
        super(Inform.class, forVariable(variable));
    }

    public QInform(Path<? extends Inform> path) {
        super(path.getType(), path.getMetadata());
    }

    public QInform(PathMetadata metadata) {
        super(Inform.class, metadata);
    }

}

