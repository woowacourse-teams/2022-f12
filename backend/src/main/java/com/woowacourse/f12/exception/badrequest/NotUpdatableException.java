package com.woowacourse.f12.exception.badrequest;

import static com.woowacourse.f12.exception.ErrorCode.INVALID_REQUEST_BODY;

public class NotUpdatableException extends InvalidValueException {

    public NotUpdatableException() {
        super(INVALID_REQUEST_BODY, "요청된 업데이트 개수와 실행된 업데이트의 수가 다릅니다.");
    }
}
