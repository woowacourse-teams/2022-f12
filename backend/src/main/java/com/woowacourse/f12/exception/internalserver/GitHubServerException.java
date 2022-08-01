package com.woowacourse.f12.exception.internalserver;

import static com.woowacourse.f12.exception.ErrorCode.EXTERNAL_SERVER_ERROR;

public class GitHubServerException extends ExternalServerException {

    public GitHubServerException() {
        super("GitHub 서버에 문제가 있습니다.", EXTERNAL_SERVER_ERROR);
    }
}
