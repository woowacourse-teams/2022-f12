package com.woowacourse.f12.exception.badrequest;


public class InvalidProfileArgumentException extends InvalidValueException {

    public InvalidProfileArgumentException() {
        super("추가 정보가 등록되지 않았습니다.");
    }
}
