package com.woowacourse.f12.exception.internalserver;

import com.woowacourse.f12.exception.ErrorCode;

public class SqlUpdateException extends InternalServerException {

    public SqlUpdateException() {
        super(ErrorCode.INTERNAL_SERVER_ERROR, "요청된 업데이트 개수와 실행된 업데이트의 수가 다릅니다.");
    }
}
