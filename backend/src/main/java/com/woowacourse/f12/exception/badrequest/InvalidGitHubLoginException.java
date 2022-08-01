package com.woowacourse.f12.exception.badrequest;

import static com.woowacourse.f12.exception.ErrorCode.INVALID_SEARCH_PARAM;

public class InvalidGitHubLoginException extends InvalidValueException {

    public InvalidGitHubLoginException() {
        super("잘못된 GitHub 로그인 요청입니다.", INVALID_SEARCH_PARAM);
    }
}
