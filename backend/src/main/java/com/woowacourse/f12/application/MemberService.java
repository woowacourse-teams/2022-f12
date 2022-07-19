package com.woowacourse.f12.application;


import com.woowacourse.f12.domain.Member;
import com.woowacourse.f12.domain.MemberRepository;
import com.woowacourse.f12.dto.response.MemberResponse;
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
        final Member member = memberRepository.findById(memberId)
                .orElseThrow(MemberNotFoundException::new);
        return MemberResponse.from(member);
    }
}
