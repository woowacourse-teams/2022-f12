package com.woowacourse.f12.exception.internalserver;

import static com.woowacourse.f12.exception.ErrorCode.INTERNAL_SERVER_ERROR;

public class IllegalRequestLogTimerState extends InternalServerException {

    public IllegalRequestLogTimerState() {
        super(INTERNAL_SERVER_ERROR, "시간을 측정할 수 없습니다.");
    }
}
