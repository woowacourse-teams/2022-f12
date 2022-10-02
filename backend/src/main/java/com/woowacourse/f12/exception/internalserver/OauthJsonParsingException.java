package com.woowacourse.f12.exception.internalserver;

import com.woowacourse.f12.exception.ErrorCode;

public class OauthJsonParsingException extends InternalServerException {
    public OauthJsonParsingException() {
        super(ErrorCode.INTERNAL_SERVER_ERROR, "Oauth 진행 중 데이터 파싱에 실패했습니다.");
    }
}
