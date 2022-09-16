package com.woowacourse.f12.presentation.auth;

import static com.woowacourse.f12.support.fixture.MemberFixture.CORINNE;
import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.verify;
import static org.mockito.Mockito.times;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.cookie;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.woowacourse.f12.application.auth.AuthService;
import com.woowacourse.f12.dto.response.auth.IssuedTokensResponse;
import com.woowacourse.f12.dto.response.auth.LoginResponse;
import com.woowacourse.f12.dto.response.auth.TokenResponse;
import com.woowacourse.f12.exception.ErrorCode;
import com.woowacourse.f12.exception.badrequest.InvalidGitHubLoginException;
import com.woowacourse.f12.exception.internalserver.GitHubServerException;
import com.woowacourse.f12.exception.unauthorized.RefreshTokenNotFoundException;
import com.woowacourse.f12.presentation.PresentationTest;
import javax.servlet.http.Cookie;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

@WebMvcTest(AuthController.class)
class AuthControllerTest extends PresentationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthService authService;

    @Test
    void 로그인_성공() throws Exception {
        // given
        String code = "code";
        String token = "token";
        String refreshToken = "refreshTokenValue";
        LoginResponse loginResponse = LoginResponse.of(token, CORINNE.생성(1L));
        TokenResponse tokenResponse = new TokenResponse(refreshToken, loginResponse);

        given(authService.login(code))
                .willReturn(tokenResponse);

        // when
        ResultActions resultActions = mockMvc.perform(
                get("/api/v1/login?code=" + code)
        );

        // then
        resultActions
                .andExpect(status().isOk())
                .andExpect(header().string("Set-cookie", containsString("refreshToken=" + refreshToken)))
                .andDo(print())
                .andDo(
                        document("auth-login")
                );

        verify(authService).login(code);
    }

    @Test
    void 로그인_실패_올바르지_않은_깃허브_코드로_로그인_요청() throws Exception {
        // given
        String code = "code";
        given(authService.login(code))
                .willThrow(new InvalidGitHubLoginException());

        // when
        ResultActions resultActions = mockMvc.perform(
                get("/api/v1/login?code=" + code)
        );

        // then
        resultActions.andExpect(status().isBadRequest())
                .andDo(print());

        verify(authService).login(code);
    }

    @Test
    void 로그인_실패_깃허브_서버_오류() throws Exception {
        // given
        String code = "code";
        given(authService.login(code))
                .willThrow(new GitHubServerException());

        // when
        ResultActions resultActions = mockMvc.perform(
                get("/api/v1/login?code=" + code)
        );

        // then
        resultActions.andExpect(status().isInternalServerError())
                .andDo(print());

        verify(authService).login(code);
    }

    @Test
    void 리프레시_토큰으로_액세스_토큰을_발급() throws Exception {
        // given
        String newAccessToken = "accessToken";
        String oldRefreshToken = "oldRefreshToken";
        String newRefreshToken = "newRefreshToken";
        given(authService.issueAccessToken(oldRefreshToken))
                .willReturn(new IssuedTokensResponse(newAccessToken, newRefreshToken));

        // when
        ResultActions resultActions = mockMvc.perform(
                post("/api/v1/accessToken")
                        .cookie(new Cookie("refreshToken", oldRefreshToken))
        );

        resultActions.andExpect(status().isOk())
                .andExpect(cookie().value("refreshToken", newRefreshToken))
                .andExpect(jsonPath("$.accessToken").value(newAccessToken))
                .andDo(
                        document("auth-issue-access-token")
                )
                .andDo(print());

        verify(authService).issueAccessToken(oldRefreshToken);
    }

    @Test
    void 리프레시_토큰없이_액세스_토큰을_발급하면_예외_발생() throws Exception {
        // given
        given(authService.issueAccessToken(any()))
                .willReturn(null);

        // when
        final ResultActions resultActions = mockMvc.perform(
                post("/api/v1/accessToken")
        );

        resultActions.andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.errorCode").value("40105"))
                .andDo(print());

        verify(authService, times(0)).issueAccessToken(any());
    }

    @Test
    void 만료된_리프레시_토큰으로_액세스_토큰_발급하면_예외_발생() throws Exception {
        // given
        given(authService.issueAccessToken(any()))
                .willThrow(new RefreshTokenNotFoundException());
        String expiredToken = "expiredToken";

        // when
        ResultActions resultActions = mockMvc.perform(
                post("/api/v1/accessToken")
                        .cookie(new Cookie("refreshToken", expiredToken))
        );

        resultActions.andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.errorCode").value(ErrorCode.REFRESH_TOKEN_NOT_FOUND.getValue()))
                .andExpect(cookie().maxAge("refreshToken", 0))
                .andDo(print());

        verify(authService).issueAccessToken(any());
    }
}
