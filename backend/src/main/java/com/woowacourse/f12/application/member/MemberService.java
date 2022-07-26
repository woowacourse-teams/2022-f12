package com.woowacourse.f12.application.member;


import com.woowacourse.f12.domain.member.Member;
import com.woowacourse.f12.domain.member.MemberRepository;
import com.woowacourse.f12.dto.request.member.MemberRequest;
import com.woowacourse.f12.dto.response.member.MemberResponse;
import com.woowacourse.f12.exception.badrequest.InvalidProfileArgumentException;
import com.woowacourse.f12.exception.notfound.MemberNotFoundException;
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
