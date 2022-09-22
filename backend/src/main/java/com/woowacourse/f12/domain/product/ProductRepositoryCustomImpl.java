package com.woowacourse.f12.domain.product;

import static com.woowacourse.f12.domain.product.QProduct.product;
import static com.woowacourse.f12.support.RepositorySupport.makeOrderSpecifiers;
import static com.woowacourse.f12.support.RepositorySupport.toContainsExpression;
import static com.woowacourse.f12.support.RepositorySupport.toEqExpression;
import static com.woowacourse.f12.support.RepositorySupport.toSlice;

import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.Collections;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;

public class ProductRepositoryCustomImpl implements ProductRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    public ProductRepositoryCustomImpl(final JPAQueryFactory jpaQueryFactory) {
        this.jpaQueryFactory = jpaQueryFactory;
    }

    @Override
    public Slice<Product> findWithoutSearchConditions(final Pageable pageable) {
        final JPAQuery<Long> jpaLongQuery = jpaQueryFactory.select(product.id)
                .from(product)
                .orderBy(makeOrderSpecifiers(product, pageable))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize() + 1);

        final Slice<Long> slice = toSlice(pageable, jpaLongQuery.fetch());
        if (slice.isEmpty()) {
            return new SliceImpl<>(Collections.emptyList(), pageable, false);
        }

        final JPAQuery<Product> jpaQuery = jpaQueryFactory.selectFrom(product)
                .where(product.id.in(slice.getContent()))
                .orderBy(makeOrderSpecifiers(product, pageable));

        return new SliceImpl<>(jpaQuery.fetch(), pageable, slice.hasNext());
    }

    @Override
    public Slice<Product> findWithSearchConditions(final String keyword, final Category category,
                                                   final Pageable pageable) {
        final JPAQuery<Product> jpaQuery = jpaQueryFactory.selectFrom(product)
                .where(
                        toEqExpression(product.category, category),
                        toContainsExpression(product.name, keyword)
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize() + 1)
                .orderBy(makeOrderSpecifiers(product, pageable));
        return toSlice(pageable, jpaQuery.fetch());
    }
}
