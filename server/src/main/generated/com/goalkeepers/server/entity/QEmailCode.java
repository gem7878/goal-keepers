package com.goalkeepers.server.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QEmailCode is a Querydsl query type for EmailCode
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QEmailCode extends EntityPathBase<EmailCode> {

    private static final long serialVersionUID = 1784011772L;

    public static final QEmailCode emailCode = new QEmailCode("emailCode");

    public final StringPath code = createString("code");

    public final StringPath email = createString("email");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public QEmailCode(String variable) {
        super(EmailCode.class, forVariable(variable));
    }

    public QEmailCode(Path<? extends EmailCode> path) {
        super(path.getType(), path.getMetadata());
    }

    public QEmailCode(PathMetadata metadata) {
        super(EmailCode.class, metadata);
    }

}

