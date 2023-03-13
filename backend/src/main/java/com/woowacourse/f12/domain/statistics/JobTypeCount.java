package com.woowacourse.f12.domain.statistics;

import com.querydsl.core.annotations.QueryProjection;
import com.woowacourse.f12.domain.member.JobType;
import com.woowacourse.f12.domain.member.MemberInfo;

public class JobTypeCount implements Countable {

    private final JobType jobType;
    private final long count;

    @QueryProjection
    public JobTypeCount(final JobType jobType, final long count) {
        this.jobType = jobType;
        this.count = count;
    }

    @Override
    public MemberInfo getValue() {
        return jobType;
    }

    @Override
    public long getCount() {
        return count;
    }
}
