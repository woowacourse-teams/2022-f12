package com.woowacourse.f12.domain.review;

import static com.woowacourse.f12.domain.member.QMember.member;
import static com.woowacourse.f12.domain.review.QReview.review;
import static com.woowacourse.f12.support.RepositorySupport.makeOrderSpecifiers;
import static com.woowacourse.f12.support.RepositorySupport.toCursorSlice;
import static java.util.Optional.ofNullable;

import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.woowacourse.f12.exception.badrequest.CursorMultipleOrderException;
import com.woowacourse.f12.support.CursorPageable;
import com.woowacourse.f12.support.CursorSlice;
import java.util.ArrayList;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;

public class ReviewRepositoryCustomImpl implements ReviewRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    public ReviewRepositoryCustomImpl(final JPAQueryFactory jpaQueryFactory) {
        this.jpaQueryFactory = jpaQueryFactory;
    }

    private static Order gerSingleOrder(final Sort sort) {
        List<Order> orders = new ArrayList<>();
        sort.iterator().forEachRemaining(orders::add);
        if (orders.size() != 1) {
            throw new CursorMultipleOrderException();
        }
        return orders.iterator().next();
    }

    private static BooleanExpression isAfterByDirection(final Long cursor, final Order order) {
        if (order.isAscending()) {
            return review.id.gt(cursor);
        }
        return review.id.lt(cursor);
    }

    @Override
    public CursorSlice<Review> findPageBy(final CursorPageable cursorPageable) {
        final Long cursor = cursorPageable.getCursor();
        final Integer size = cursorPageable.getSize();
        final Sort sort = cursorPageable.getSort();
        final List<Review> reviews = jpaQueryFactory.select(review)
                .from(review)
                .where(afterCursor(cursor, sort))
                .limit(size)
                .orderBy(makeOrderSpecifiers(review, sort))
                .fetch();
        return toCursorSlice(size, reviews);
    }

    private Predicate afterCursor(final Long cursor, final Sort sort) {
        final Order order = gerSingleOrder(sort);
        return ofNullable(cursor)
                .map(value -> isAfterByDirection(cursor, order))
                .orElse(null);
    }

    @Override
    public List<CareerLevelCount> findCareerLevelCountByProductId(final Long productId) {
        return jpaQueryFactory.select(new QCareerLevelCount(member.careerLevel, member.id.count()))
                .from(review)
                .innerJoin(review.member, member)
                .where(
                        review.product.id.eq(productId),
                        member.careerLevel.isNotNull(),
                        member.jobType.isNotNull()
                )
                .groupBy(member.careerLevel)
                .fetch();
    }

    @Override
    public List<JobTypeCount> findJobTypeCountByProductId(final Long productId) {
        return jpaQueryFactory.select(new QJobTypeCount(member.jobType, member.id.count()))
                .from(review)
                .innerJoin(review.member, member)
                .where(
                        review.product.id.eq(productId),
                        member.careerLevel.isNotNull(),
                        member.jobType.isNotNull()
                )
                .groupBy(member.jobType)
                .fetch();
    }
}
