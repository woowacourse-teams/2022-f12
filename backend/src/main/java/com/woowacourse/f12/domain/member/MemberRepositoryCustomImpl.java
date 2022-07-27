package com.woowacourse.f12.domain.member;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

public class MemberRepositoryCustomImpl extends QuerydslRepositorySupport implements MemberRepositoryCustom {

    private final QMember member = QMember.member;
    private final JPAQueryFactory jpaQueryFactory;

    public MemberRepositoryCustomImpl(final JPAQueryFactory jpaQueryFactory) {
        super(Member.class);
        this.jpaQueryFactory = jpaQueryFactory;
    }

    public Slice<Member> findByContains(final String keyword, final CareerLevel careerLevel, final JobType jobType,
                                        final Pageable pageable) {
        final JPAQuery<Member> jpaQuery = jpaQueryFactory.select(member)
                .from(member)
                .where(
                        containsKeyword(keyword),
                        eqCareerLevel(careerLevel),
                        eqJobType(jobType)
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize() + 1);
        final List<Member> members = jpaQuery.fetch();
        return toSlice(pageable, members);
    }

    private BooleanExpression containsKeyword(final String keyword) {
        if (keyword == null || keyword.isBlank()) {
            return null;
        }
        return member.gitHubId.contains(keyword);
    }

    public BooleanExpression eqCareerLevel(final CareerLevel careerLevel) {
        if (careerLevel == null) {
            return null;
        }
        return member.careerLevel.eq(careerLevel);
    }

    public BooleanExpression eqJobType(final JobType jobType) {
        if (jobType == null) {
            return null;
        }
        return member.jobType.eq(jobType);
    }

    private SliceImpl<Member> toSlice(final Pageable pageable, final List<Member> members) {
        if (members.size() > pageable.getPageSize()) {
            members.remove(members.size() - 1);
            return new SliceImpl<>(members, pageable, true);
        }

        return new SliceImpl<>(members, pageable, false);
    }
}
