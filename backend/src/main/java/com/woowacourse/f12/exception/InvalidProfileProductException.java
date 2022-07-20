package com.woowacourse.f12.exception;

public class InvalidProfileProductException extends InvalidValueException {

    public InvalidProfileProductException() {
        super("유효하지 않은 대표 장비 수정 요청입니다.");
    }
}
