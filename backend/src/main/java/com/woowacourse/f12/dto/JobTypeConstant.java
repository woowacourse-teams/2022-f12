package com.woowacourse.f12.dto;

import com.woowacourse.f12.domain.member.JobType;
import com.woowacourse.f12.exception.badrequest.InvalidJobTypeException;
import com.woowacourse.f12.presentation.ViewConstant;
import java.util.Arrays;
import lombok.Getter;

@Getter
public enum JobTypeConstant implements ViewConstant {

    FRONTEND("frontend", JobType.FRONTEND),
    BACKEND("backend", JobType.BACKEND),
    MOBILE("mobile", JobType.MOBILE),
    ETC("etc", JobType.ETC);

    private final String value;
    private final JobType jobType;

    JobTypeConstant(final String value, final JobType jobType) {
        this.value = value;
        this.jobType = jobType;
    }

    public static JobTypeConstant findByViewValue(final String source) {
        return Arrays.stream(values())
                .filter(jobType -> jobType.value.equals(source))
                .findFirst()
                .orElseThrow(InvalidJobTypeException::new);
    }

    public static JobTypeConstant findByJobType(final JobType jobType) {
        return Arrays.stream(values())
                .filter(it -> it.jobType.equals(jobType))
                .findFirst()
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
