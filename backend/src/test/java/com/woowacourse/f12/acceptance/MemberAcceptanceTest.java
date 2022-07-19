package com.woowacourse.f12.acceptance;

import static com.woowacourse.f12.acceptance.support.RestAssuredRequestUtil.GET_요청을_보낸다;
import static com.woowacourse.f12.acceptance.support.RestAssuredRequestUtil.로그인된_상태로_GET_요청을_보낸다;
import static org.assertj.core.api.Assertions.assertThat;

import com.woowacourse.f12.dto.response.LoginResponse;
import com.woowacourse.f12.dto.response.MemberResponse;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

public class MemberAcceptanceTest extends AcceptanceTest {

    @Test
    void 로그인_된_상태에서_내_회원정보를_조회한다() {
        // given
        LoginResponse loginResponse = GET_요청을_보낸다("/api/v1/login?code=dkasjbdkjas")
                .as(LoginResponse.class);
        String token = loginResponse.getToken();
        MemberResponse memberResponse = loginResponse.getMember();

        // when
        ExtractableResponse<Response> response = 로그인된_상태로_GET_요청을_보낸다("/api/v1/members/me", token);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.as(MemberResponse.class)).usingRecursiveComparison()
                .isEqualTo(memberResponse);
    }

    @Test
    void 비로그인_상태에서_회원정보를_조회한다() {
        // given
        LoginResponse loginResponse = GET_요청을_보낸다("/api/v1/login?code=dkasjbdkjas")
                .as(LoginResponse.class);
        MemberResponse memberResponse = loginResponse.getMember();

        // when
        ExtractableResponse<Response> response = GET_요청을_보낸다("/api/v1/members/" + memberResponse.getId());

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.as(MemberResponse.class)).usingRecursiveComparison()
                .isEqualTo(memberResponse);
    }
}
