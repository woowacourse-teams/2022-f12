package com.woowacourse.f12.domain.review;

import static com.woowacourse.f12.domain.member.QMember.member;
import static com.woowacourse.f12.domain.review.QReview.review;
import static com.woowacourse.f12.support.RepositorySupport.toCursorSlice;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.woowacourse.f12.support.CursorSlice;
import java.util.List;

public class ReviewRepositoryCustomImpl implements ReviewRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    public ReviewRepositoryCustomImpl(final JPAQueryFactory jpaQueryFactory) {
        this.jpaQueryFactory = jpaQueryFactory;
    }

    private static BooleanExpression afterThan(final Long cursor) {
        if (cursor == null) {
            return Expressions.TRUE.isTrue();
        }
        return review.id.lt(cursor);
    }

    @Override
    public CursorSlice<Review> findRecentPageBy(final Long cursor, final Integer size) {
        final List<Review> reviews = jpaQueryFactory.select(review)
                .from(review)
                .where(afterThan(cursor))
                .limit(size)
                .orderBy(review.id.desc())
                .fetch();
        return toCursorSlice(size, reviews);
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
