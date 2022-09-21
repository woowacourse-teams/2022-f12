package com.woowacourse.f12.domain.product;

import static com.woowacourse.f12.domain.product.QProduct.product;
import static com.woowacourse.f12.support.RepositorySupport.makeOrderSpecifiers;
import static com.woowacourse.f12.support.RepositorySupport.toContainsExpression;
import static com.woowacourse.f12.support.RepositorySupport.toEqExpression;
import static com.woowacourse.f12.support.RepositorySupport.toSlice;

import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.ArrayList;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;

public class ProductRepositoryCustomImpl implements ProductRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    public ProductRepositoryCustomImpl(final JPAQueryFactory jpaQueryFactory) {
        this.jpaQueryFactory = jpaQueryFactory;
    }

    @Override
    public Slice<Product> findBySearchConditions(final String keyword, final Category category,
                                                 final Pageable pageable) {
        final JPAQuery<Product> jpaQuery = jpaQueryFactory.selectFrom(product)
                .where(
                        toContainsExpression(product.name, keyword),
                        toEqExpression(product.category, category)
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize() + 1)
                .orderBy(makeOrderSpecifiers(product, pageable));
        return toSlice(pageable, jpaQuery.fetch());
    }

    @Override
    public Slice<Product> findWithoutSearchConditions(final Pageable pageable) {
        final List<Long> ids = jpaQueryFactory.select(product.id)
                .from(product)
                .orderBy(makeOrderSpecifiers(product, pageable))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize() + 1)
                .fetch();

        if (ids.isEmpty()) {
            return new SliceImpl<>(new ArrayList<>(), pageable, false);
        }

        final JPAQuery<Product> jpaQuery = jpaQueryFactory.selectFrom(product)
                .where(product.id.in(ids))
                .orderBy(makeOrderSpecifiers(product, pageable));
        return toSlice(pageable, jpaQuery.fetch());
    }
}
