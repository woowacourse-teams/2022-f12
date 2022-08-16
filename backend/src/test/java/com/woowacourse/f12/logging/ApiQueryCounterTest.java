package com.woowacourse.f12.logging;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class ApiQueryCounterTest {

    @Test
    void 쿼리_개수를_증가시킨다() {
        // given
        ApiQueryCounter apiQueryCounter = new ApiQueryCounter();

        // when
        apiQueryCounter.increaseCount();

        // then
        assertThat(apiQueryCounter.getCount()).isEqualTo(1);
    }
}
