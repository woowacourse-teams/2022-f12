package com.woowacourse.f12.dto;

import com.woowacourse.f12.domain.member.CareerLevel;
import com.woowacourse.f12.exception.badrequest.InvalidCareerLevelException;
import com.woowacourse.f12.presentation.ViewConstant;
import java.util.Arrays;
import lombok.Getter;

@Getter
public enum CareerLevelConstant implements ViewConstant {

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

    public static CareerLevelConstant findByViewValue(final String source) {
        return Arrays.stream(values())
                .filter(careerLevel -> careerLevel.value.equals(source))
                .findFirst()
                .orElseThrow(InvalidCareerLevelException::new);
    }

    public static CareerLevelConstant findByCareerLevel(final CareerLevel careerLevel) {
        return Arrays.stream(values())
                .filter(it -> it.careerLevel.equals(careerLevel))
                .findFirst()
                .orElseThrow(InvalidCareerLevelException::new);
    }

    public CareerLevel toCareerLevel() {
        return this.careerLevel;
    }

    @Override
    public String getViewValue() {
        return this.value;
    }
}
