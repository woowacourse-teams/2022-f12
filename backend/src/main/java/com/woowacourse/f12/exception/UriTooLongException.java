package com.woowacourse.f12.exception;

public class UriTooLongException extends CustomException {
    public UriTooLongException(final int maxLength) {
        super(ErrorCode.URI_TOO_LONG, String.format("요청 URL의 길이가 최대 길이(%d자)를 넘을 수 없습니다.", maxLength));
    }
}
