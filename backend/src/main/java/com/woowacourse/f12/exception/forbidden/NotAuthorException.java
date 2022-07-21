package com.woowacourse.f12.exception.forbidden;

public class NotAuthorException extends ForbiddenMemberException {

    public NotAuthorException() {
        super("리뷰의 작성자가 아닙니다.");
    }
}
