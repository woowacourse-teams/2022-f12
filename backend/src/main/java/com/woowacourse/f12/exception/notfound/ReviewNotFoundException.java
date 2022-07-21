package com.woowacourse.f12.exception.notfound;

public class ReviewNotFoundException extends NotFoundException {

    public ReviewNotFoundException() {
        super("리뷰를 찾을 수 없습니다.");
    }
}
