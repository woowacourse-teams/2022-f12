package com.woowacourse.f12.dto;

import static com.woowacourse.f12.domain.member.JobType.BACKEND;
import static com.woowacourse.f12.domain.member.JobType.ETC;
import static com.woowacourse.f12.domain.member.JobType.FRONTEND;
import static com.woowacourse.f12.domain.member.JobType.MOBILE;

import com.woowacourse.f12.domain.member.JobType;
import com.woowacourse.f12.exception.badrequest.InvalidJobTypeException;
import com.woowacourse.f12.presentation.ViewConstant;
import java.util.Arrays;
import lombok.Getter;

@Getter
public enum JobTypeConstant implements ViewConstant {

    FRONTEND_CONSTANT("frontend", FRONTEND),
    BACKEND_CONSTANT("backend", BACKEND),
    MOBILE_CONSTANT("mobile", MOBILE),
    ETC_CONSTANT("etc", ETC);

    private final String value;
    private final JobType jobType;

    JobTypeConstant(final String value, final JobType jobType) {
        this.value = value;
        this.jobType = jobType;
    }

    public static JobTypeConstant findByViewValue(final String source) {
        return Arrays.stream(values())
                .filter(jobType -> jobType.value.equals(source))
                .findAny()
                .orElseThrow(InvalidJobTypeException::new);
    }

    public static JobTypeConstant from(final JobType jobType) {
        return Arrays.stream(values())
                .filter(it -> it.jobType.equals(jobType))
                .findAny()
                .orElseThrow(InvalidJobTypeException::new);
    }

    public JobType toJobType() {
        return this.jobType;
    }

    @Override
    public String getViewValue() {
        return this.value;
    }
}
