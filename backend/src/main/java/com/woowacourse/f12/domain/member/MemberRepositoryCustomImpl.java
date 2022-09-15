package com.woowacourse.f12.domain.member;

import static com.woowacourse.f12.domain.member.QFollowing.following;
import static com.woowacourse.f12.domain.member.QMember.member;
import static com.woowacourse.f12.support.RepositorySupport.makeOrderSpecifiers;
import static com.woowacourse.f12.support.RepositorySupport.toContainsExpression;
import static com.woowacourse.f12.support.RepositorySupport.toEqExpression;
import static com.woowacourse.f12.support.RepositorySupport.toSlice;

import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public class MemberRepositoryCustomImpl implements MemberRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    public MemberRepositoryCustomImpl(final JPAQueryFactory jpaQueryFactory) {
        this.jpaQueryFactory = jpaQueryFactory;
    }

    public Slice<Member> findBySearchConditions(final String keyword, final CareerLevel careerLevel,
                                                final JobType jobType,
                                                final Pageable pageable) {
        final JPAQuery<Member> jpaQuery = jpaQueryFactory.selectFrom(member)
                .where(
                        toContainsExpression(member.gitHubId, keyword),
                        toEqExpression(member.careerLevel, careerLevel),
                        toEqExpression(member.jobType, jobType),
                        member.careerLevel.isNotNull(),
                        member.jobType.isNotNull()
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize() + 1)
                .orderBy(makeOrderSpecifiers(member, pageable));

        return toSlice(pageable, jpaQuery.fetch());
    }

    public Slice<Member> findFollowingsBySearchConditions(final Long loggedInId, final String keyword,
                                                          final CareerLevel careerLevel,
                                                          final JobType jobType, final Pageable pageable) {
        final JPAQuery<Member> jpaQuery = jpaQueryFactory.selectFrom(member)
                .join(following)
                .on(member.id.eq(following.followingId))
                .where(
                        following.followerId.eq(loggedInId),
                        toContainsExpression(member.gitHubId, keyword),
                        toEqExpression(member.careerLevel, careerLevel),
                        toEqExpression(member.jobType, jobType),
                        member.careerLevel.isNotNull(),
                        member.jobType.isNotNull()
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize() + 1)
                .orderBy(makeOrderSpecifiers(member, pageable));

        return toSlice(pageable, jpaQuery.fetch());
    }
}
