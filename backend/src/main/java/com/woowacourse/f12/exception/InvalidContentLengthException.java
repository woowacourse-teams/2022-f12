package com.woowacourse.f12.exception;

public class InvalidContentLengthException extends IllegalArgumentException {

    public InvalidContentLengthException(final int maxLength) {
        super("내용의 길이는 " + maxLength + "자 이하 여야 합니다.");
    }
}
