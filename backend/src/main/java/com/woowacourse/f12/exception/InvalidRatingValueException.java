package com.woowacourse.f12.exception;

public class InvalidRatingValueException extends IllegalArgumentException {

    public InvalidRatingValueException() {
        super("평점은 1에서 5 사이여야 합니다.");
    }
}
