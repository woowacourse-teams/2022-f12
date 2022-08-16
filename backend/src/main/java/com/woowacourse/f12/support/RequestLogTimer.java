package com.woowacourse.f12.support;

import com.woowacourse.f12.exception.internalserver.IllegalRequestLogTimerState;
import java.time.Clock;
import java.util.Objects;
import org.springframework.web.context.annotation.RequestScope;

@RequestScope
public class RequestLogTimer {

    private final Clock clock;

    private Long startTime;
    private Long endTime;

    public RequestLogTimer(final Clock clock) {
        this.clock = clock;
    }

    public void start() {
        this.startTime = clock.millis();
    }

    public void stop() {
        validateTime(startTime);
        this.endTime = clock.millis();
    }

    public Long getSpentTime() {
        validateTime(endTime);
        return endTime - startTime;
    }

    private void validateTime(final Long time) {
        if (Objects.isNull(time)) {
            throw new IllegalRequestLogTimerState();
        }
    }
}
