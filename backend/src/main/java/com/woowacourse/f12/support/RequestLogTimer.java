package com.woowacourse.f12.support;

import com.woowacourse.f12.exception.internalserver.IllegalRequestLogTimerState;
import java.util.Objects;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

@Component
@RequestScope
public class RequestLogTimer {

    private Long startTime;
    private Long endTime;

    public void start() {
        this.startTime = System.currentTimeMillis();
    }

    public void stop() {
        validateTime(startTime);
        this.endTime = System.currentTimeMillis();
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
