package com.woowacourse.f12.application;


import com.woowacourse.f12.domain.Member;
import com.woowacourse.f12.domain.MemberRepository;
import com.woowacourse.f12.dto.request.MemberRequest;
import com.woowacourse.f12.dto.response.MemberResponse;
import com.woowacourse.f12.exception.InvalidProfileArgumentException;
import com.woowacourse.f12.exception.MemberNotFoundException;
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
        member.updateCareerLevel(memberRequest.getCareerLevel());
        member.updateJobType(memberRequest.getJobType());
    }

    private Member findMember(final Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(MemberNotFoundException::new);
    }
}
