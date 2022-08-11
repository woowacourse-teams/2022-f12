package com.woowacourse.f12.exception.badrequest;

import static com.woowacourse.f12.exception.ErrorCode.INVALID_LOGIN_CODE;

public class InvalidGitHubLoginException extends InvalidValueException {

    public InvalidGitHubLoginException() {
        super(INVALID_LOGIN_CODE, "잘못된 GitHub 로그인 요청입니다.");
    }
}
