package com.woowacourse.f12.dto;

import com.woowacourse.f12.domain.member.CareerLevel;
import lombok.Getter;

@Getter
public enum CareerLevelConstant {

    NONE("none", CareerLevel.NONE),
    JUNIOR("junior", CareerLevel.JUNIOR),
    MID_LEVEL("midlevel", CareerLevel.MID_LEVEL),
    SENIOR("senior", CareerLevel.SENIOR);

    private final String value;
    private final CareerLevel careerLevel;

    CareerLevelConstant(final String value, final CareerLevel careerLevel) {
        this.value = value;
        this.careerLevel = careerLevel;
    }

    public CareerLevel toCareerLevel() {
        return this.careerLevel;
    }
}
