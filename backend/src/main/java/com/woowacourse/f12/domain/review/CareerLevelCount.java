package com.woowacourse.f12.domain.review;

import com.querydsl.core.annotations.QueryProjection;
import com.woowacourse.f12.domain.member.CareerLevel;
import lombok.Getter;

@Getter
public class CareerLevelCount {

    private final CareerLevel careerLevel;
    private final long count;

    @QueryProjection
    public CareerLevelCount(final CareerLevel careerLevel, final long count) {
        this.careerLevel = careerLevel;
        this.count = count;
    }
}
