package com.woowacourse.f12.exception;

public class InvalidPageNumberFormatException extends InvalidValueException {

    public InvalidPageNumberFormatException() {
        super("페이지 번호는 숫자 형식이여야 합니다.");
    }
}
