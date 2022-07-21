package com.woowacourse.f12.documentation;

import static com.woowacourse.f12.domain.CareerLevel.JUNIOR;
import static com.woowacourse.f12.domain.JobType.BACK_END;
import static com.woowacourse.f12.support.MemberFixtures.CORINNE;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.woowacourse.f12.application.JwtProvider;
import com.woowacourse.f12.application.MemberService;
import com.woowacourse.f12.dto.request.MemberRequest;
import com.woowacourse.f12.dto.response.MemberResponse;
import com.woowacourse.f12.presentation.MemberController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

@WebMvcTest(MemberController.class)
public class MemberDocumentation extends Documentation {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MemberService memberService;

    @MockBean
    private JwtProvider jwtProvider;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void 로그인된_상태에서_나의_회원정보를_조회_API_문서화() throws Exception {
        // given
        String authorizationHeader = "Bearer Token";
        given(jwtProvider.validateToken(authorizationHeader))
                .willReturn(true);
        given(jwtProvider.getPayload(authorizationHeader))
                .willReturn("1");
        given(memberService.findById(1L))
                .willReturn(MemberResponse.from(CORINNE.생성(1L)));

        // when
        ResultActions resultActions = mockMvc.perform(
                get("/api/v1/members/me")
                        .header(HttpHeaders.AUTHORIZATION, authorizationHeader)
        );

        // then
        resultActions.andExpect(status().isOk())
                .andDo(document("members-get-own"))
                .andDo(print());
    }

    @Test
    void 비로그인_상태에서_회원정보를_조회_API_문서화() throws Exception {
        // given
        Long memberId = 1L;
        given(memberService.findById(memberId))
                .willReturn(MemberResponse.from(CORINNE.생성(memberId)));

        // when
        ResultActions resultActions = mockMvc.perform(
                get("/api/v1/members/" + memberId)
        );

        // then
        resultActions.andExpect(status().isOk())
                .andDo(document("members-get-by-memberId"))
                .andDo(print());
    }

    @Test
    void 로그인된_상태에서_나의_회원정보를_수정_API_문서화() throws Exception {
        // given
        MemberRequest memberRequest = new MemberRequest(JUNIOR, BACK_END);
        String authorizationHeader = "Bearer Token";
        given(jwtProvider.validateToken(authorizationHeader))
                .willReturn(true);
        given(jwtProvider.getPayload(authorizationHeader))
                .willReturn("1");
        willDoNothing().given(memberService).updateMember(1L, memberRequest);

        // when
        ResultActions resultActions = mockMvc.perform(
                patch("/api/v1/members/me")
                        .header(HttpHeaders.AUTHORIZATION, authorizationHeader)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(memberRequest))
        );

        // then
        resultActions.andExpect(status().isOk())
                .andDo(document("members-update"))
                .andDo(print());
    }
}
