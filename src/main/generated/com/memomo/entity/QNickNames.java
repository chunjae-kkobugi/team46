package com.memomo.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QNickNames is a Querydsl query type for NickNames
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QNickNames extends EntityPathBase<NickNames> {

    private static final long serialVersionUID = -887786721L;

    public static final QNickNames nickNames = new QNickNames("nickNames");

    public final NumberPath<Integer> bno = createNumber("bno", Integer.class);

    public final StringPath cookie = createString("cookie");

    public final StringPath nickname = createString("nickname");

    public QNickNames(String variable) {
        super(NickNames.class, forVariable(variable));
    }

    public QNickNames(Path<? extends NickNames> path) {
        super(path.getType(), path.getMetadata());
    }

    public QNickNames(PathMetadata metadata) {
        super(NickNames.class, metadata);
    }

}

