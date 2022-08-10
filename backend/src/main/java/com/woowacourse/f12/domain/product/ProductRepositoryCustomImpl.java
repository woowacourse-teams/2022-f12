package com.woowacourse.f12.domain.product;

import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import static com.woowacourse.f12.domain.product.QProduct.product;
import static com.woowacourse.f12.support.RepositorySupport.*;

public class ProductRepositoryCustomImpl implements ProductRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    public ProductRepositoryCustomImpl(final JPAQueryFactory jpaQueryFactory) {
        this.jpaQueryFactory = jpaQueryFactory;
    }

    @Override
    public Slice<Product> findBySearchConditions(final String keyword, final Category category, final Pageable pageable) {
        final JPAQuery<Product> jpaQuery = jpaQueryFactory.selectFrom(product)
                .where(
                        containsKeyword(product.name, keyword),
                        toEqExpression(product.category, category)
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize() + 1)
                .orderBy(makeOrderSpecifiers(product, pageable));
        return toSlice(pageable, jpaQuery.fetch());
    }
}
