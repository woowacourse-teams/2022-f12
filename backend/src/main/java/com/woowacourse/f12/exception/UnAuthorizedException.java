package com.woowacourse.f12.exception;

public class UnAuthorizedException extends RuntimeException {

    public UnAuthorizedException() {
        super("로그인이 필요합니다.");
    }
}
