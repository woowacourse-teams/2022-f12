package com.woowacourse.f12.exception.badrequest;


import static com.woowacourse.f12.exception.ErrorCode.NOT_ENOUGH_DATA;

public class InvalidProfileArgumentException extends InvalidValueException {

    public InvalidProfileArgumentException() {
        super(NOT_ENOUGH_DATA, "추가 정보가 등록되지 않았습니다.");
    }
}
