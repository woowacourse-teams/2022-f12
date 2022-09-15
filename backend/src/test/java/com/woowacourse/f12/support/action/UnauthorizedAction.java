package com.woowacourse.f12.support.action;

import static com.woowacourse.f12.acceptance.support.RestAssuredRequestUtil.GET_요청을_보낸다;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

public class UnauthorizedAction {

    private UnauthorizedAction() {
    }

    public static UnauthorizedAction 로그인하지_않고() {
        return new UnauthorizedAction();
    }

    public ExtractableResponse<Response> 리뷰_목록_페이지를_조회한다(final int page, final int size, final String sortCondition) {
        return GET_요청을_보낸다(String.format("/api/v1/reviews?page=%d&size=%d&sort=%s,desc", page, size, sortCondition));
    }
}
