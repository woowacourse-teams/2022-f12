package com.woowacourse.f12.exception;

public class KeyboardNotFoundException extends NotFoundException {

    public KeyboardNotFoundException() {
        super("키보드를 찾을 수 없습니다.");
    }
}
