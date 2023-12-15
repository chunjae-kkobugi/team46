package com.memomo.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QBoard is a Querydsl query type for Board
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QBoard extends EntityPathBase<Board> {

    private static final long serialVersionUID = 1041283424L;

    public static final QBoard board = new QBoard("board");

    public final QBaseEntity _super = new QBaseEntity(this);

    public final StringPath bgColor = createString("bgColor");

    public final NumberPath<Integer> bgImage = createNumber("bgImage", Integer.class);

    public final NumberPath<Integer> bno = createNumber("bno", Integer.class);

    public final StringPath bpw = createString("bpw");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createAt = _super.createAt;

    public final StringPath layout = createString("layout");

    public final NumberPath<Integer> maxStudent = createNumber("maxStudent", Integer.class);

    public final NumberPath<Long> postHead = createNumber("postHead", Long.class);

    public final StringPath status = createString("status");

    public final StringPath teacher = createString("teacher");

    public final StringPath title = createString("title");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updateAt = _super.updateAt;

    public QBoard(String variable) {
        super(Board.class, forVariable(variable));
    }

    public QBoard(Path<? extends Board> path) {
        super(path.getType(), path.getMetadata());
    }

    public QBoard(PathMetadata metadata) {
        super(Board.class, metadata);
    }

}

