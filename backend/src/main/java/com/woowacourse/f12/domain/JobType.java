package com.woowacourse.f12.domain;

import lombok.Getter;

@Getter
public enum JobType {

    FRONT_END("프론트엔드"),
    BACK_END("백엔드"),
    MOBILE("모바일 (안드로이드, iOS)"),
    ETC("기타");

    private final String value;

    JobType(final String value) {
        this.value = value;
    }
}
