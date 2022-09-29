package com.woowacourse.f12.exception.internalserver;

import com.woowacourse.f12.exception.ErrorCode;

public class OauthProcessingException extends InternalServerException {
    public OauthProcessingException() {
        super(ErrorCode.INTERNAL_SERVER_ERROR, "Oauth 진행 중 예상치 못한 문제가 생겼습니다.");
    }
}
