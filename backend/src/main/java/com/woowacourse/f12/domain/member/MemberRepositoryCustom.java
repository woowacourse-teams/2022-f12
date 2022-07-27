package com.woowacourse.f12.domain.member;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface MemberRepositoryCustom {

    Slice<Member> findByContains(String keyword, CareerLevel careerLevel, JobType jobType, Pageable pageable);
}
