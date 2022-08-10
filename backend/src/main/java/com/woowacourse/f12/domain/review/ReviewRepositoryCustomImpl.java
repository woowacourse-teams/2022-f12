package com.woowacourse.f12.domain.review;

import static com.woowacourse.f12.domain.member.QMember.member;
import static com.woowacourse.f12.domain.review.QReview.review;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.woowacourse.f12.domain.product.Product;
import java.util.List;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

public class ReviewRepositoryCustomImpl extends QuerydslRepositorySupport implements ReviewRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    public ReviewRepositoryCustomImpl(final JPAQueryFactory jpaQueryFactory) {
        super(Product.class);
        this.jpaQueryFactory = jpaQueryFactory;
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
