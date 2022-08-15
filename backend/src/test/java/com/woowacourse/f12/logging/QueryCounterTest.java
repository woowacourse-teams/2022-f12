package com.woowacourse.f12.logging;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class QueryCounterTest {

    @Test
    void count를_증가시킨다() {
        // given
        QueryCounter queryCounter = new QueryCounter();

        // when
        queryCounter.increaseCount();

        // then
        assertThat(queryCounter.getCount()).isEqualTo(1);
    }
}
