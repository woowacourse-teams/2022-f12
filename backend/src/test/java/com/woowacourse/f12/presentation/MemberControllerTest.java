package com.woowacourse.f12.presentation;

import static com.woowacourse.f12.domain.CareerLevel.JUNIOR;
import static com.woowacourse.f12.domain.JobType.ETC;
import static com.woowacourse.f12.support.MemberFixtures.CORINNE;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.woowacourse.f12.application.JwtProvider;
import com.woowacourse.f12.application.MemberService;
import com.woowacourse.f12.dto.request.MemberRequest;
import com.woowacourse.f12.dto.response.MemberResponse;
import com.woowacourse.f12.exception.MemberNotFoundException;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(MemberController.class)
class MemberControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MemberService memberService;

    @MockBean
    private JwtProvider jwtProvider;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void 로그인된_상태에서_나의_회원정보를_조회_성공() throws Exception {
        // given
        String authorizationHeader = "Bearer Token";
        given(jwtProvider.validateToken(authorizationHeader))
                .willReturn(true);
        given(jwtProvider.getPayload(authorizationHeader))
                .willReturn("1");
        given(memberService.findById(1L))
                .willReturn(MemberResponse.from(CORINNE.생성(1L)));

        // when
        mockMvc.perform(
                        get("/api/v1/members/me")
                                .header(HttpHeaders.AUTHORIZATION, authorizationHeader)
                ).andExpect(status().isOk())
                .andDo(print());

        // then
        assertAll(
                () -> verify(jwtProvider).validateToken(authorizationHeader),
                () -> verify(jwtProvider).getPayload(authorizationHeader),
                () -> verify(memberService).findById(1L)
        );
    }

    @Test
    void 비로그인_상태에서_회원정보를_조회_성공() throws Exception {
        // given
        Long memberId = 1L;
        given(memberService.findById(memberId))
                .willReturn(MemberResponse.from(CORINNE.생성(memberId)));

        // when
        mockMvc.perform(
                        get("/api/v1/members/" + memberId)
                ).andExpect(status().isOk())
                .andDo(print());

        // then
        verify(memberService).findById(1L);
    }

    @Test
    void 비로그인_상태에서_회원정보를_조회_실패_등록되지_않은_회원일_경우() throws Exception {
        // given
        Long memberId = 1L;
        given(memberService.findById(memberId))
                .willThrow(new MemberNotFoundException());

        // when
        mockMvc.perform(
                        get("/api/v1/members/" + memberId)
                ).andExpect(status().isNotFound())
                .andDo(print());

        // then
        verify(memberService).findById(1L);
    }

    @Test
    void 로그인된_상태에서_나의_회원정보를_수정_성공() throws Exception {
        // given
        MemberRequest memberRequest = new MemberRequest(JUNIOR, ETC);
        String authorizationHeader = "Bearer Token";
        given(jwtProvider.validateToken(authorizationHeader))
                .willReturn(true);
        given(jwtProvider.getPayload(authorizationHeader))
                .willReturn("1");
        willDoNothing().given(memberService).updateMember(1L, memberRequest);

        // when
        mockMvc.perform(
                        patch("/api/v1/members/me")
                                .header(HttpHeaders.AUTHORIZATION, authorizationHeader)
                                .content(objectMapper.writeValueAsString(memberRequest))
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                )
                .andExpect(status().isOk())
                .andDo(print());

        // then
        assertAll(
                () -> verify(jwtProvider).validateToken(authorizationHeader),
                () -> verify(jwtProvider).getPayload(authorizationHeader),
                () -> verify(memberService).updateMember(eq(1L), any(MemberRequest.class))
        );
    }

    @Test
    void 로그인된_상태에서_나의_회원정보를_수정_성공_Enum의_name값으로_요청을_보낼때() throws Exception {
        // given
        Map<String, Object> memberRequest = new HashMap<>();
        memberRequest.put("careerLevel", "SENIOR");
        memberRequest.put("jobType", "BACK_END");

        String authorizationHeader = "Bearer Token";
        given(jwtProvider.validateToken(authorizationHeader))
                .willReturn(true);
        given(jwtProvider.getPayload(authorizationHeader))
                .willReturn("1");
        willDoNothing().given(memberService).updateMember(eq(1L), any(MemberRequest.class));

        // when
        mockMvc.perform(
                        patch("/api/v1/members/me")
                                .header(HttpHeaders.AUTHORIZATION, authorizationHeader)
                                .content(objectMapper.writeValueAsString(memberRequest))
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                )
                .andExpect(status().isOk())
                .andDo(print());

        // then
        assertAll(
                () -> verify(jwtProvider).validateToken(authorizationHeader),
                () -> verify(jwtProvider).getPayload(authorizationHeader),
                () -> verify(memberService).updateMember(eq(1L), any(MemberRequest.class))
        );
    }
}
