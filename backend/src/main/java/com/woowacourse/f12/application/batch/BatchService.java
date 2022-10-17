package com.woowacourse.f12.application.batch;

import com.woowacourse.f12.domain.member.MemberRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class BatchService {

    private final MemberRepository memberRepository;

    public BatchService(final MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Transactional
    public void updateFollowerCount() {
        memberRepository.updateFollowerCountBatch();
    }
}
