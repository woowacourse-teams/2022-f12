package com.woowacourse.f12.dto;

import static com.woowacourse.f12.domain.member.CareerLevel.JUNIOR;
import static com.woowacourse.f12.domain.member.CareerLevel.MID_LEVEL;
import static com.woowacourse.f12.domain.member.CareerLevel.NONE;
import static com.woowacourse.f12.domain.member.CareerLevel.SENIOR;

import com.woowacourse.f12.domain.member.CareerLevel;
import com.woowacourse.f12.exception.badrequest.InvalidCareerLevelException;
import com.woowacourse.f12.presentation.ViewConstant;
import java.util.Arrays;
import lombok.Getter;

@Getter
public enum CareerLevelConstant implements ViewConstant {

    NONE_CONSTANT("none", NONE),
    JUNIOR_CONSTANT("junior", JUNIOR),
    MID_LEVEL_CONSTANT("midlevel", MID_LEVEL),
    SENIOR_CONSTANT("senior", SENIOR);

    private final String value;
    private final CareerLevel careerLevel;

    CareerLevelConstant(final String value, final CareerLevel careerLevel) {
        this.value = value;
        this.careerLevel = careerLevel;
    }

    public static CareerLevelConstant findByViewValue(final String source) {
        return Arrays.stream(values())
                .filter(careerLevel -> careerLevel.value.equals(source))
                .findAny()
                .orElseThrow(InvalidCareerLevelException::new);
    }

    public static CareerLevelConstant findByCareerLevel(final CareerLevel careerLevel) {
        return Arrays.stream(values())
                .filter(it -> it.careerLevel.equals(careerLevel))
                .findAny()
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
