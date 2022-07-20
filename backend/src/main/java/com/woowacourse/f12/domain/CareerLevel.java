package com.woowacourse.f12.domain;

import lombok.Getter;

@Getter
public enum CareerLevel {

    NONE("경력 없음"),
    JUNIOR("0~2년차"),
    MID_LEVEL("3~5년차"),
    SENIOR("5년차 이상");

    private final String value;

    CareerLevel(final String value) {
        this.value = value;
    }
}
