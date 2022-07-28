package com.woowacourse.f12.exception.badrequest;

public class InvalidJobTypeException extends InvalidValueException {

    public InvalidJobTypeException() {
        super("올바르지 않은 직군 입력입니다.");
    }
}
