package com.memomo.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QBoardGroup is a Querydsl query type for BoardGroup
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QBoardGroup extends EntityPathBase<BoardGroup> {

    private static final long serialVersionUID = 1684678911L;

    public static final QBoardGroup boardGroup = new QBoardGroup("boardGroup");

    public final NumberPath<Integer> bno = createNumber("bno", Integer.class);

    public final StringPath gColor = createString("gColor");

    public final NumberPath<Integer> gno = createNumber("gno", Integer.class);

    public final StringPath title = createString("title");

    public QBoardGroup(String variable) {
        super(BoardGroup.class, forVariable(variable));
    }

    public QBoardGroup(Path<? extends BoardGroup> path) {
        super(path.getType(), path.getMetadata());
    }

    public QBoardGroup(PathMetadata metadata) {
        super(BoardGroup.class, metadata);
    }

}

