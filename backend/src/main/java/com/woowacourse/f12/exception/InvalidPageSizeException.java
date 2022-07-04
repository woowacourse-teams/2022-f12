package com.woowacourse.f12.exception;

public class InvalidPageSizeException extends IllegalArgumentException {

    public InvalidPageSizeException(int maxSize) {
        super("페이지의 크기는" + maxSize + "이하여야 합니다.");
    }
}
