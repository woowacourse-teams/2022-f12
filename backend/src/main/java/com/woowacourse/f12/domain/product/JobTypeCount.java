package com.woowacourse.f12.domain.product;

import com.querydsl.core.annotations.QueryProjection;
import com.woowacourse.f12.domain.member.JobType;

public class JobTypeCount {

    private final JobType jobType;
    private final long count;

    @QueryProjection
    public JobTypeCount(final JobType jobType, final long count) {
        this.jobType = jobType;
        this.count = count;
    }
}
