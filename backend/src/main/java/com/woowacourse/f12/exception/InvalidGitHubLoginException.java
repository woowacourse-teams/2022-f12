package com.woowacourse.f12.exception;

public class InvalidGitHubLoginException extends InvalidValueException {

    public InvalidGitHubLoginException() {
        super("잘못된 GitHub 로그인 요청입니다.");
    }
}
