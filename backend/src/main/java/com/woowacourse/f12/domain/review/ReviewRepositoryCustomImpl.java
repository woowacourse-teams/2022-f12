package com.woowacourse.f12.domain.review;

import static com.woowacourse.f12.domain.member.QMember.member;
import static com.woowacourse.f12.domain.product.QProduct.product;
import static com.woowacourse.f12.domain.review.QReview.review;
import static com.woowacourse.f12.support.RepositorySupport.makeOrderSpecifiers;
import static com.woowacourse.f12.support.RepositorySupport.toSlice;

import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.Collections;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;

public class ReviewRepositoryCustomImpl implements ReviewRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    public ReviewRepositoryCustomImpl(final JPAQueryFactory jpaQueryFactory) {
        this.jpaQueryFactory = jpaQueryFactory;
    }

    @Override
    public Slice<Review> findPageBy(final Pageable pageable) {
        final JPAQuery<Long> coveringIndexQuery = jpaQueryFactory.select(review.id)
                .from(review)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize() + 1)
                .orderBy(makeOrderSpecifiers(review, pageable));
        final Slice<Long> reviewIds = toSlice(pageable, coveringIndexQuery.fetch());

        if (reviewIds.isEmpty()) {
            return new SliceImpl<>(Collections.emptyList(), pageable, false);
        }

        final JPAQuery<Review> query = jpaQueryFactory.selectFrom(review)
                .where(review.id.in(reviewIds.getContent()))
                .innerJoin(review.member, member)
                .fetchJoin()
                .innerJoin(review.product, product)
                .fetchJoin()
                .orderBy(makeOrderSpecifiers(review, pageable));
        return new SliceImpl<>(query.fetch(), pageable, reviewIds.hasNext());
    }
}
