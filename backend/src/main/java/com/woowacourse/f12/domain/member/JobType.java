package com.woowacourse.f12.domain.member;

import lombok.Getter;

@Getter
public enum JobType implements MemberInfo {

    FRONTEND,
    BACKEND,
    MOBILE,
    ETC;
}
