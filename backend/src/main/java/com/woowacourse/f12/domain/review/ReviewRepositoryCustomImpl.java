package com.woowacourse.f12.domain.review;

import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.woowacourse.f12.domain.member.QMember;
import com.woowacourse.f12.domain.product.Product;
import com.woowacourse.f12.domain.product.QProduct;
import java.util.List;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

public class ReviewRepositoryCustomImpl extends QuerydslRepositorySupport implements ReviewRepositoryCustom {

    private final QProduct product = QProduct.product;
    private final QReview review = QReview.review;
    private final QMember member = QMember.member;
    private final JPAQueryFactory jpaQueryFactory;

    public ReviewRepositoryCustomImpl(final JPAQueryFactory jpaQueryFactory) {
        super(Product.class);
        this.jpaQueryFactory = jpaQueryFactory;
    }

    @Override
    public List<CareerLevelCount> findCareerLevelCountByProductId(final Long productId) {
        final JPAQuery<CareerLevelCount> jpaQuery = jpaQueryFactory.from(review)
                .innerJoin(review.member, member)
                .where(review.product.id.eq(productId))
                .groupBy(member.careerLevel)
                .select(
                        new QCareerLevelCount(member.careerLevel, member.id.count())
                );
        return jpaQuery.fetch();
    }

    @Override
    public List<JobTypeCount> findJobTypeCountByProductId(final Long productId) {
        final JPAQuery<JobTypeCount> jpaQuery = jpaQueryFactory.from(review)
                .innerJoin(review.member, member)
                .where(review.product.id.eq(productId))
                .groupBy(member.jobType)
                .select(
                        new QJobTypeCount(member.jobType, member.id.count())
                );
        return jpaQuery.fetch();
    }
}
