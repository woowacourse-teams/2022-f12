package com.woowacourse.f12.acceptance.support;

import static com.woowacourse.f12.acceptance.support.RestAssuredRequestUtil.GET_요청을_보낸다;

import com.woowacourse.f12.dto.response.LoginResponse;

public class LoginUtil {

    private LoginUtil() {
    }

    public static LoginResponse 로그인을_한다(String code) {
        return GET_요청을_보낸다("/api/v1/login?code=" + code)
                .as(LoginResponse.class);
    }
}
