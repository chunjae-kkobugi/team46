package com.memomo.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QPostFile is a Querydsl query type for PostFile
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QPostFile extends EntityPathBase<PostFile> {

    private static final long serialVersionUID = 1766595970L;

    public static final QPostFile postFile = new QPostFile("postFile");

    public final StringPath fileType = createString("fileType");

    public final NumberPath<Long> fno = createNumber("fno", Long.class);

    public final StringPath fstatus = createString("fstatus");

    public final StringPath originName = createString("originName");

    public final NumberPath<Long> pno = createNumber("pno", Long.class);

    public final StringPath saveName = createString("saveName");

    public final StringPath savePath = createString("savePath");

    public QPostFile(String variable) {
        super(PostFile.class, forVariable(variable));
    }

    public QPostFile(Path<? extends PostFile> path) {
        super(path.getType(), path.getMetadata());
    }

    public QPostFile(PathMetadata metadata) {
        super(PostFile.class, metadata);
    }

}

