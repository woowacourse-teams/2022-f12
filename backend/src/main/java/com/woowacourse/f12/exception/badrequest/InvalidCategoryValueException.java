package com.woowacourse.f12.exception.badrequest;

public class InvalidCategoryValueException extends InvalidValueException {

    public InvalidCategoryValueException() {
        super("잘못된 카테고리입니다.");
    }
}
