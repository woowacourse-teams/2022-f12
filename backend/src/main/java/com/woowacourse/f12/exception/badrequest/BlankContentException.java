package com.woowacourse.f12.exception.badrequest;

public class BlankContentException extends InvalidValueException {

    public BlankContentException() {
        super("리뷰 내용은 공백이 될 수 없습니다.");
    }
}
