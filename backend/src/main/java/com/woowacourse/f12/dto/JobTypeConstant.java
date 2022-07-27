package com.woowacourse.f12.dto;

import com.woowacourse.f12.domain.member.JobType;
import lombok.Getter;

@Getter
public enum JobTypeConstant {

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

    public JobType toJobType() {
        return this.jobType;
    }
}
