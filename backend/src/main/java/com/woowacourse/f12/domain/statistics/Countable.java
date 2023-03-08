package com.woowacourse.f12.domain.statistics;

import com.woowacourse.f12.domain.member.MemberInfo;

public interface Countable {

    MemberInfo getValue();

    long getCount();
}
