package com.woowacourse.f12.domain.member;

import static com.woowacourse.f12.domain.member.QFollowing.following;
import static com.woowacourse.f12.domain.member.QMember.member;
import static com.woowacourse.f12.support.RepositorySupport.makeOrderSpecifiers;
import static com.woowacourse.f12.support.RepositorySupport.toContainsExpression;
import static com.woowacourse.f12.support.RepositorySupport.toEqExpression;
import static com.woowacourse.f12.support.RepositorySupport.toSlice;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.Collections;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;

public class MemberRepositoryCustomImpl implements MemberRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    public MemberRepositoryCustomImpl(final JPAQueryFactory jpaQueryFactory) {
        this.jpaQueryFactory = jpaQueryFactory;
    }

    public Slice<Member> findWithOutSearchConditions(final Pageable pageable) {
        final JPAQuery<Long> query = jpaQueryFactory.select(member.id)
                .from(member)
                .where(toEqExpression(member.registered, true))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize() + 1)
                .orderBy(makeOrderSpecifiers(member, pageable));
        final Slice<Long> memberIds = toSlice(pageable, query.fetch());

        if (memberIds.isEmpty()) {
            return new SliceImpl<>(Collections.emptyList(), pageable, false);
        }

        final JPAQuery<Member> jpaQuery = jpaQueryFactory.selectFrom(member)
                .where(member.id.in(memberIds.getContent()))
                .orderBy(makeOrderSpecifiers(member, pageable));

        return new SliceImpl<>(jpaQuery.fetch(), pageable, memberIds.hasNext());
    }

    public Slice<Member> findWithSearchConditions(final String keyword, final CareerLevel careerLevel,
                                                  final JobType jobType,
                                                  final Pageable pageable) {
        final JPAQuery<Member> jpaQuery = jpaQueryFactory.selectFrom(member)
                .where(
                        eqCareerLevel(careerLevel),
                        eqJobType(jobType),
                        toContainsExpression(member.gitHubId, keyword)
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize() + 1)
                .orderBy(makeOrderSpecifiers(member, pageable));

        return toSlice(pageable, jpaQuery.fetch());
    }

    private BooleanExpression eqCareerLevel(final CareerLevel careerLevel) {
        if (careerLevel == null) {
            return member.careerLevel.isNotNull();
        }
        return member.careerLevel.eq(careerLevel);
    }

    private BooleanExpression eqJobType(final JobType jobType) {
        if (jobType == null) {
            return member.jobType.isNotNull();
        }
        return member.jobType.eq(jobType);
    }

    public Slice<Member> findFollowingsWithOutSearchConditions(final Long loggedInId, final Pageable pageable) {
        final List<Long> followingMemberIds = findFollowingMemberIds(loggedInId);
        final JPAQuery<Member> jpaQuery = jpaQueryFactory.selectFrom(member)
                .where(
                        member.id.in(followingMemberIds),
                        toEqExpression(member.registered, true)
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize() + 1)
                .orderBy(makeOrderSpecifiers(member, pageable));

        return toSlice(pageable, jpaQuery.fetch());
    }

    public Slice<Member> findFollowingsWithSearchConditions(final Long loggedInId, final String keyword,
                                                            final CareerLevel careerLevel,
                                                            final JobType jobType,
                                                            final Pageable pageable) {
        final List<Long> followingMemberIds = findFollowingMemberIds(loggedInId);
        final JPAQuery<Member> jpaQuery = jpaQueryFactory.selectFrom(member)
                .where(
                        member.id.in(followingMemberIds),
                        eqCareerLevel(careerLevel),
                        eqJobType(jobType),
                        toContainsExpression(member.gitHubId, keyword)
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize() + 1)
                .orderBy(makeOrderSpecifiers(member, pageable));

        return toSlice(pageable, jpaQuery.fetch());
    }

    private List<Long> findFollowingMemberIds(final Long loggedInId) {
        return jpaQueryFactory.select(following.followingId)
                .from(following)
                .where(
                        following.followerId.eq(loggedInId)
                ).fetch();
    }
}
