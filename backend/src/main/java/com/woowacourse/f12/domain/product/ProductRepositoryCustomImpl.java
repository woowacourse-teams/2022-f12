package com.woowacourse.f12.domain.product;

import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.woowacourse.f12.domain.product.QProduct.product;

public class ProductRepositoryCustomImpl implements ProductRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    public ProductRepositoryCustomImpl(final JPAQueryFactory jpaQueryFactory) {
        this.jpaQueryFactory = jpaQueryFactory;
    }

    @Override
    public Slice<Product> findBySearchConditions(final String keyword, final Category category, final Pageable pageable) {
        final JPAQuery<Product> jpaQuery = jpaQueryFactory.selectFrom(product)
                .where(
                        containsKeyword(keyword),
                        eqCategory(category)
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize() + 1)
                .orderBy(makeOrderSpecifiers(pageable));
        return toSlice(pageable, jpaQuery.fetch());
    }

    private BooleanExpression containsKeyword(final String keyword) {
        if (Objects.isNull(keyword) || keyword.isBlank()) {
            return null;
        }
        return product.name.contains(keyword);
    }

    private BooleanExpression eqCategory(final Category category) {
        if (Objects.isNull(category)) {
            return null;
        }
        return product.category.eq(category);
    }

    private OrderSpecifier[] makeOrderSpecifiers(final Pageable pageable) {
        return pageable.getSort()
                .stream()
                .map(this::toOrderSpecifier)
                .collect(Collectors.toList()).toArray(OrderSpecifier[]::new);
    }

    private OrderSpecifier toOrderSpecifier(final Sort.Order sortOrder) {
        final Order orderMethod = toOrder(sortOrder);
        final PathBuilder<Product> pathBuilder = new PathBuilder<>(product.getType(), product.getMetadata());
        return new OrderSpecifier(orderMethod, pathBuilder.get(sortOrder.getProperty()));
    }

    private Order toOrder(final Sort.Order sortOrder) {
        if (sortOrder.isAscending()) {
            return Order.ASC;
        }
        return Order.DESC;
    }

    private Slice<Product> toSlice(final Pageable pageable, final List<Product> products) {
        if (products.size() > pageable.getPageSize()) {
            products.remove(products.size() - 1);
            return new SliceImpl<>(products, pageable, true);
        }
        return new SliceImpl<>(products, pageable, false);
    }
}
