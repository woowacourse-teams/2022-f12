package com.woowacourse.f12.exception;

public class BlankContentException extends IllegalArgumentException {

    public BlankContentException() {
        super("리뷰 내용은 공백이 될 수 없습니다.");
    }
}
