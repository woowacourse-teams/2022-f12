package com.woowacourse.f12.domain.member;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface MemberRepositoryCustom {

    Slice<Member> findWithOutSearchConditions(Pageable pageable);

    Slice<Member> findWithOnlyOptions(CareerLevel careerLevel, JobType jobType, Pageable pageable);

    Slice<Member> findWithSearchConditions(String keyword, CareerLevel careerLevel, JobType jobType, Pageable pageable);

    Slice<Member> findFollowingsWithOutSearchConditions(Long loggedInId, Pageable pageable);

    Slice<Member> findFollowingsWithOnlyOptions(Long loggedInId, CareerLevel careerLevel, JobType jobType,
                                                Pageable pageable);

    Slice<Member> findFollowingsWithSearchConditions(Long loggedInId, String keyword, CareerLevel careerLevel,
                                                     JobType jobType,
                                                     Pageable pageable);
}
