package com.woowacourse.f12.support;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.woowacourse.f12.exception.internalserver.IllegalRequestLogTimerState;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RequestLogTimerTest {

    private RequestLogTimer requestLogTimer;

    @BeforeEach
    void setUp() {
        requestLogTimer = new RequestLogTimer();
    }

    @Test
    void 측정_시작과_끝_사이_걸리는_시간을_측정한다() throws InterruptedException {
        // given
        requestLogTimer.start();
        Thread.sleep(5000);
        requestLogTimer.stop();

        // when
        Long timeTaken = requestLogTimer.getSpentTime();

        // then
        assertThat(timeTaken).isGreaterThan(4999);
    }

    @Test
    void 시작하지_않았는데_측정_종료하는_경우_예외_발생한다() {
        // given, when, then
        assertThatThrownBy(() -> requestLogTimer.stop())
                .isExactlyInstanceOf(IllegalRequestLogTimerState.class);
    }

    @Test
    void 측정_종료하지_않고_측정_시간을_구하면_예외_발생한다() {
        // given
        requestLogTimer.start();

        // when, then
        assertThatThrownBy(() -> requestLogTimer.getSpentTime())
                .isExactlyInstanceOf(IllegalRequestLogTimerState.class);
    }
}
