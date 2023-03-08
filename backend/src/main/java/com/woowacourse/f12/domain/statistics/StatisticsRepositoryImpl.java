package com.woowacourse.f12.domain.statistics;

import static com.woowacourse.f12.domain.member.QMember.member;
import static com.woowacourse.f12.domain.review.QReview.review;

import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class StatisticsRepositoryImpl implements StatisticsRepository {
    private final JPAQueryFactory jpaQueryFactory;

    public StatisticsRepositoryImpl(final JPAQueryFactory jpaQueryFactory) {
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
