package com.woowacourse.f12.exception.badrequest;

import com.woowacourse.f12.exception.ErrorCode;

public class CursorMultipleOrderException extends InvalidValueException {
    public CursorMultipleOrderException() {
        super(ErrorCode.CURSOR_MULTIPLE_ORDER, "커서 기반 페이징에는 정렬기준이 여러개일 수 없습니다.");
    }
}
