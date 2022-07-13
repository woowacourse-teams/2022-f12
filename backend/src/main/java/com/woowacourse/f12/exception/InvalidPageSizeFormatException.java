package com.woowacourse.f12.exception;

public class InvalidPageSizeFormatException extends InvalidValueException {

    public InvalidPageSizeFormatException() {
        super("패이지 크기는 숫자 형식이어야 합니다.");
    }
}
