package com.memomo.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QBoardFile is a Querydsl query type for BoardFile
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QBoardFile extends EntityPathBase<BoardFile> {

    private static final long serialVersionUID = -361336068L;

    public static final QBoardFile boardFile = new QBoardFile("boardFile");

    public final NumberPath<Integer> bno = createNumber("bno", Integer.class);

    public final StringPath fileType = createString("fileType");

    public final NumberPath<Integer> fno = createNumber("fno", Integer.class);

    public final StringPath originName = createString("originName");

    public final StringPath saveName = createString("saveName");

    public final StringPath savePath = createString("savePath");

    public final StringPath status = createString("status");

    public QBoardFile(String variable) {
        super(BoardFile.class, forVariable(variable));
    }

    public QBoardFile(Path<? extends BoardFile> path) {
        super(path.getType(), path.getMetadata());
    }

    public QBoardFile(PathMetadata metadata) {
        super(BoardFile.class, metadata);
    }

}

