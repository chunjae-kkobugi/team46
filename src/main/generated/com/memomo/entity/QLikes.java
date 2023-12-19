package com.memomo.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QLikes is a Querydsl query type for Likes
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QLikes extends EntityPathBase<Likes> {

    private static final long serialVersionUID = 1050349110L;

    public static final QLikes likes = new QLikes("likes");

    public final StringPath author = createString("author");

    public final NumberPath<Integer> bno = createNumber("bno", Integer.class);

    public final NumberPath<Long> lno = createNumber("lno", Long.class);

    public final NumberPath<Long> pno = createNumber("pno", Long.class);

    public QLikes(String variable) {
        super(Likes.class, forVariable(variable));
    }

    public QLikes(Path<? extends Likes> path) {
        super(path.getType(), path.getMetadata());
    }

    public QLikes(PathMetadata metadata) {
        super(Likes.class, metadata);
    }

}

