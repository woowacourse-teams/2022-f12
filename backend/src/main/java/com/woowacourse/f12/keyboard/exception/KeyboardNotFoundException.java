package com.woowacourse.f12.keyboard.exception;

import java.util.NoSuchElementException;

public class KeyboardNotFoundException extends NoSuchElementException {

    public KeyboardNotFoundException() {
        super("키보드를 찾을 수 없습니다.");
    }
}
