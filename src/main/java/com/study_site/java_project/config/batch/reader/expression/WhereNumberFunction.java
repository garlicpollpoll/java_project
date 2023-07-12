package com.study_site.java_project.config.batch.reader.expression;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.NumberPath;

public interface WhereNumberFunction<N extends Number & Comparable<?>> {

    BooleanExpression apply(NumberPath<N> id, int page, N currentId);
}
