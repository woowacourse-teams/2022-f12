package com.woowacourse.f12.domain.product;

import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.woowacourse.f12.domain.member.QMember;
import com.woowacourse.f12.domain.review.QReview;
import java.util.List;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

public class ProductRepositoryCustomImpl extends QuerydslRepositorySupport implements ProductRepositoryCustom {

    private final QProduct product = QProduct.product;
    private final QReview review = QReview.review;
    private final QMember member = QMember.member;
    private final JPAQueryFactory jpaQueryFactory;

    public ProductRepositoryCustomImpl(final JPAQueryFactory jpaQueryFactory) {
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
}
