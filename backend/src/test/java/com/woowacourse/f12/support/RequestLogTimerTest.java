package com.woowacourse.f12.support;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.woowacourse.f12.exception.internalserver.IllegalRequestLogTimerState;
import java.time.Clock;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class RequestLogTimerTest {

    @Mock
    private Clock clock;

    @InjectMocks
    private RequestLogTimer requestLogTimer;

    @Test
    void 측정_시작과_끝_사이_걸리는_시간을_측정한다() throws InterruptedException {
        // given
        given(clock.millis())
                .willReturn(1L, 2L);

        // when
        requestLogTimer.start();
        requestLogTimer.stop();
        Long timeTaken = requestLogTimer.getSpentTime();

        // then
        assertAll(
                () -> assertThat(timeTaken).isGreaterThanOrEqualTo(1),
                () -> verify(clock, times(2)).millis()
        );
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
        given(clock.millis())
                .willReturn(1L);
        requestLogTimer.start();

        // when, then
        assertThatThrownBy(() -> requestLogTimer.getSpentTime())
                .isExactlyInstanceOf(IllegalRequestLogTimerState.class);
    }
}
