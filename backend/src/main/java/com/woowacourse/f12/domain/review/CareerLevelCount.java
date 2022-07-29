package com.woowacourse.f12.domain.review;

import com.querydsl.core.annotations.QueryProjection;
import com.woowacourse.f12.domain.member.CareerLevel;

public class CareerLevelCount implements Countable {

    private final CareerLevel careerLevel;
    private final long count;

    @QueryProjection
    public CareerLevelCount(final CareerLevel careerLevel, final long count) {
        this.careerLevel = careerLevel;
        this.count = count;
    }

    @Override
    public Enum getValue() {
        return careerLevel;
    }

    @Override
    public long getCount() {
        return count;
    }
}
