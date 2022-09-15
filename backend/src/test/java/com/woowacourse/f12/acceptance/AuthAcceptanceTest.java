package com.woowacourse.f12.acceptance;

import static com.woowacourse.f12.acceptance.support.RestAssuredRequestUtil.GET_요청을_보낸다;
import static com.woowacourse.f12.support.GitHubProfileFixtures.CORINNE_GITHUB;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.woowacourse.f12.dto.response.auth.LoginResponse;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

class AuthAcceptanceTest extends AcceptanceTest {

    @Test
    void 로그인_요청이_들어오고_OAUTH_인증에_성공하면_토큰과_회원정보를_반환한다() {
        // given, when
        ExtractableResponse<Response> response = GET_요청을_보낸다("/api/v1/login?code=" + CORINNE_GITHUB.getCode());

        // then
        final LoginResponse loginResponse = response.as(LoginResponse.class);
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(loginResponse).isNotNull(),
                () -> assertThat(loginResponse.getToken()).isNotNull(),
                () -> assertThat(response.header("Set-cookie")).isNotNull()
        );
    }
}
