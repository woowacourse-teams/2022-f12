package com.woowacourse.f12.exception.badrequest;

public class InvalidCareerLevelException extends InvalidValueException {

    public InvalidCareerLevelException() {
        super("올바르지 않은 연차 입력입니다.");
    }
}
