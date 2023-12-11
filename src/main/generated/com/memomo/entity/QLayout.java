package com.memomo.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QLayout is a Querydsl query type for Layout
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QLayout extends EntityPathBase<Layout> {

    private static final long serialVersionUID = -1805877264L;

    public static final QLayout layout = new QLayout("layout");

    public final NumberPath<Integer> gno = createNumber("gno", Integer.class);

    public final NumberPath<Integer> gPriority = createNumber("gPriority", Integer.class);

    public final NumberPath<Long> lno = createNumber("lno", Long.class);

    public final NumberPath<Long> pno = createNumber("pno", Long.class);

    public final NumberPath<Long> priority = createNumber("priority", Long.class);

    public final NumberPath<Integer> x = createNumber("x", Integer.class);

    public final NumberPath<Integer> y = createNumber("y", Integer.class);

    public final NumberPath<Integer> z = createNumber("z", Integer.class);

    public QLayout(String variable) {
        super(Layout.class, forVariable(variable));
    }

    public QLayout(Path<? extends Layout> path) {
        super(path.getType(), path.getMetadata());
    }

    public QLayout(PathMetadata metadata) {
        super(Layout.class, metadata);
    }

}

