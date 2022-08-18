package com.woowacourse.f12.domain.member;

import com.woowacourse.f12.exception.badrequest.SelfFollowException;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class FollowingTest {

    @Test
    void 자기_자신을_팔로우할_수는_없다() {
        // given
        Long memberId = 1L;

        // when, then
        assertThatThrownBy(
                () -> Following.builder()
                        .followerId(memberId)
                        .followeeId(memberId)
                        .build()
        ).isExactlyInstanceOf(SelfFollowException.class);
    }
}
