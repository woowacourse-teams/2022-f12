package com.woowacourse.f12.acceptance;

import static com.woowacourse.f12.acceptance.support.RestAssuredRequestUtil.GET_요청을_보낸다;
import static com.woowacourse.f12.support.fixture.GitHubProfileFixture.CORINNE_GITHUB;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.woowacourse.f12.dto.response.auth.IssuedTokensResponse;
import com.woowacourse.f12.dto.response.auth.LoginResponse;
import io.restassured.RestAssured;
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
        LoginResponse loginResponse = response.as(LoginResponse.class);
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(loginResponse).isNotNull(),
                () -> assertThat(loginResponse.getToken()).isNotNull(),
                () -> assertThat(response.header("Set-cookie")).isNotNull()
        );
    }

    @Test
    void 리프레시_토큰으로_엑세스_토큰과_리프레시_토큰_재발급한다() {
        // given
        ExtractableResponse<Response> response = GET_요청을_보낸다(
                "/api/v1/login?code=" + CORINNE_GITHUB.getCode());
        String refreshToken = response.header("Set-cookie").split("=")[1];

        // when
        String url = "/api/v1/accessToken";
        final ExtractableResponse<Response> refreshTokenResponse = RestAssured.given().log().all()
                .when()
                .cookie("refreshToken", refreshToken)
                .post(url)
                .then().log().all()
                .extract();

        // then
        assertAll(
                () -> assertThat(refreshTokenResponse.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(refreshTokenResponse.as(IssuedTokensResponse.class).getAccessToken()).isNotNull(),
                () -> assertThat(refreshTokenResponse.header("Set-cookie")).contains("refreshToken")
        );
    }
}
