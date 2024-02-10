package com.goalkeepers.server.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QSetting is a Querydsl query type for Setting
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QSetting extends EntityPathBase<Setting> {

    private static final long serialVersionUID = 1458217827L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QSetting setting = new QSetting("setting");

    public final BooleanPath commentAlarm = createBoolean("commentAlarm");

    public final BooleanPath contentLikeAlarm = createBoolean("contentLikeAlarm");

    public final BooleanPath ddayAlarm = createBoolean("ddayAlarm");

    public final BooleanPath goalShareAlarm = createBoolean("goalShareAlarm");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final QMember member;

    public final BooleanPath postCheerAlarm = createBoolean("postCheerAlarm");

    public final BooleanPath todayAlarm = createBoolean("todayAlarm");

    public QSetting(String variable) {
        this(Setting.class, forVariable(variable), INITS);
    }

    public QSetting(Path<? extends Setting> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QSetting(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QSetting(PathMetadata metadata, PathInits inits) {
        this(Setting.class, metadata, inits);
    }

    public QSetting(Class<? extends Setting> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.member = inits.isInitialized("member") ? new QMember(forProperty("member"), inits.get("member")) : null;
    }

}

