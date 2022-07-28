package com.woowacourse.f12.application.member;


import com.woowacourse.f12.domain.member.CareerLevel;
import com.woowacourse.f12.domain.member.JobType;
import com.woowacourse.f12.domain.member.Member;
import com.woowacourse.f12.domain.member.MemberRepository;
import com.woowacourse.f12.dto.CareerLevelConstant;
import com.woowacourse.f12.dto.JobTypeConstant;
import com.woowacourse.f12.dto.request.member.MemberRequest;
import com.woowacourse.f12.dto.request.member.MemberSearchRequest;
import com.woowacourse.f12.dto.response.member.MemberPageResponse;
import com.woowacourse.f12.dto.response.member.MemberResponse;
import com.woowacourse.f12.exception.badrequest.InvalidProfileArgumentException;
import com.woowacourse.f12.exception.notfound.MemberNotFoundException;
import java.util.Objects;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class MemberService {

    private final MemberRepository memberRepository;

    public MemberService(final MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public MemberResponse findById(final Long memberId) {
        final Member member = findMember(memberId);
        if (!member.isRegisterCompleted()) {
            throw new InvalidProfileArgumentException();
        }
        return MemberResponse.from(member);
    }

    @Transactional
    public void updateMember(final Long memberId, final MemberRequest memberRequest) {
        final Member member = findMember(memberId);
        member.updateCareerLevel(memberRequest.getCareerLevel().toCareerLevel());
        member.updateJobType(memberRequest.getJobType().toJobType());
    }

    private Member findMember(final Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(MemberNotFoundException::new);
    }


    public MemberPageResponse findByContains(final MemberSearchRequest memberSearchRequest, final Pageable pageable) {
        final CareerLevel careerLevel = parseCareerLevel(memberSearchRequest);
        final JobType jobType = parseJobType(memberSearchRequest);
        final Slice<Member> slice = memberRepository.findBySearchConditions(memberSearchRequest.getQuery(), careerLevel,
                jobType, pageable);
        return MemberPageResponse.from(slice);
    }

    private JobType parseJobType(final MemberSearchRequest memberSearchRequest) {
        final JobTypeConstant jobTypeConstant = memberSearchRequest.getJobType();
        if (Objects.isNull(jobTypeConstant)) {
            return null;
        }
        return jobTypeConstant.toJobType();
    }

    private CareerLevel parseCareerLevel(final MemberSearchRequest memberSearchRequest) {
        final CareerLevelConstant careerLevelConstant = memberSearchRequest.getCareerLevel();
        if (Objects.isNull(careerLevelConstant)) {
            return null;
        }
        return careerLevelConstant.toCareerLevel();
    }
}
