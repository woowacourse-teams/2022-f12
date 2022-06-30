package com.woowacourse.f12.acceptance.support;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.MediaType;

public class RestAssuredRequestUtil {

    private RestAssuredRequestUtil() {
    }

    public static ExtractableResponse<Response> GET_요청을_보낸다(final String url) {
        return RestAssured.given().log().all()
                .when()
                .get(url)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> POST_요청을_보낸다(final String url, final Object requestBody) {
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(requestBody)
                .when()
                .post(url)
                .then().log().all()
                .extract();
    }
}
