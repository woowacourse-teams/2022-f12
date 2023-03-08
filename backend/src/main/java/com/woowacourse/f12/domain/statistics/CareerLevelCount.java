package com.woowacourse.f12.domain.statistics;

import com.querydsl.core.annotations.QueryProjection;
import com.woowacourse.f12.domain.member.CareerLevel;
import com.woowacourse.f12.domain.member.MemberInfo;

public class CareerLevelCount implements Countable {

    private final CareerLevel careerLevel;
    private final long count;

    @QueryProjection
    public CareerLevelCount(final CareerLevel careerLevel, final long count) {
        this.careerLevel = careerLevel;
        this.count = count;
    }

    @Override
    public MemberInfo getValue() {
        return careerLevel;
    }

    @Override
    public long getCount() {
        return count;
    }
}
