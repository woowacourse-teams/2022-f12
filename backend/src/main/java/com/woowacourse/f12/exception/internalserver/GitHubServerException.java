package com.woowacourse.f12.exception.internalserver;

public class GitHubServerException extends ExternalServerException {

    public GitHubServerException() {
        super("GitHub 서버에 문제가 있습니다.");
    }
}
