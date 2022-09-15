package com.woowacourse.f12.application.auth;

import org.apache.logging.log4j.core.util.UuidUtil;
import org.springframework.stereotype.Component;

@Component
public class RefreshTokenProvider {

    public String createToken() {
        return UuidUtil.getTimeBasedUuid().toString();
    }
}
