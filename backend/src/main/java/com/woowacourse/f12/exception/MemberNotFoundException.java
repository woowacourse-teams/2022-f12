package com.woowacourse.f12.exception;

public class MemberNotFoundException extends NotFoundException {

    public MemberNotFoundException() {
        super("회원을 찾을 수 없습니다.");
    }
}
