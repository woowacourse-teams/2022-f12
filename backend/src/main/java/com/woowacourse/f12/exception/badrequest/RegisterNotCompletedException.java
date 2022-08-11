package com.woowacourse.f12.exception.badrequest;


import static com.woowacourse.f12.exception.ErrorCode.REGISTER_NOT_COMPLETED;

public class RegisterNotCompletedException extends InvalidValueException {

    public RegisterNotCompletedException() {
        super(REGISTER_NOT_COMPLETED, "추가 정보가 등록되지 않았습니다.");
    }
}
