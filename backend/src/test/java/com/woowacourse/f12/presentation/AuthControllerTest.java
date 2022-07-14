package com.woowacourse.f12.presentation;

import static com.woowacourse.f12.support.MemberFixtures.CORINNE;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.woowacourse.f12.application.AuthService;
import com.woowacourse.f12.dto.response.LoginResponse;
import com.woowacourse.f12.exception.GitHubServerException;
import com.woowacourse.f12.exception.InvalidGitHubLoginException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(AuthController.class)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthService authService;

    @Test
    void 로그인_성공() throws Exception {
        // given
        String code = "code";
        String token = "token";
        LoginResponse loginResponse = LoginResponse.of(token, CORINNE.생성(1L));
        given(authService.login(code))
                .willReturn(loginResponse);

        // when
        mockMvc.perform(
                        get("/api/v1/login?code=" + code)
                ).andExpect(status().isOk())
                .andDo(print());

        // then
        verify(authService).login(code);
    }

    @Test
    void 로그인_실패_올바르지_않은_로그인_요청() throws Exception {
        // given
        String code = "code";
        given(authService.login(code))
                .willThrow(new InvalidGitHubLoginException());

        // when
        mockMvc.perform(
                        get("/api/v1/login?code=" + code)
                ).andExpect(status().isBadRequest())
                .andDo(print());

        // then
        verify(authService).login(code);
    }

    @Test
    void 로그인_실패_깃허브_서버_오류() throws Exception {
        // given
        String code = "code";
        given(authService.login(code))
                .willThrow(new GitHubServerException());

        // when
        mockMvc.perform(
                        get("/api/v1/login?code=" + code)
                ).andExpect(status().isInternalServerError())
                .andDo(print());

        // then
        verify(authService).login(code);
    }
}
