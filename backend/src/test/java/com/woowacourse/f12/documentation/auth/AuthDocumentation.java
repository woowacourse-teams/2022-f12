package com.woowacourse.f12.documentation.auth;

import static com.woowacourse.f12.support.MemberFixtures.CORINNE;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.woowacourse.f12.application.auth.AuthService;
import com.woowacourse.f12.documentation.Documentation;
import com.woowacourse.f12.domain.member.Member;
import com.woowacourse.f12.dto.response.auth.LoginResponse;
import com.woowacourse.f12.presentation.auth.AuthController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

@WebMvcTest(AuthController.class)
class AuthDocumentation extends Documentation {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthService authService;

    @Test
    void 로그인_API_문서화() throws Exception {
        // given
        String code = "code";
        String token = "applicationAccessToken";
        Member member = CORINNE.생성(1L);
        given(authService.login(code))
                .willReturn(LoginResponse.of(token, member));

        // when
        ResultActions resultActions = mockMvc.perform(
                get("/api/v1/login?code=" + code)
        );

        // then
        resultActions.andExpect(status().isOk())
                .andDo(print())
                .andDo(
                        document("auth-login")
                );
    }
}
