package com.woowacourse.f12.documentation;

import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.woowacourse.f12.application.JwtProvider;
import com.woowacourse.f12.application.MemberService;
import com.woowacourse.f12.dto.request.ProfileProductRequest;
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
class MemberDocumentation extends Documentation {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MemberService memberService;

    @MockBean
    private JwtProvider jwtProvider;

    private ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void 대표_장비를_등록하는_API_문서화() throws Exception {
        // given
        String authorizationHeader = "Bearer Token";
        given(jwtProvider.validateToken(authorizationHeader))
                .willReturn(true);
        given(jwtProvider.getPayload(authorizationHeader))
                .willReturn("1");
        Long memberId = 1L;
        ProfileProductRequest profileProductRequest = new ProfileProductRequest(1L, 2L);
        willDoNothing().given(memberService).updateProfileProducts(memberId, profileProductRequest);

        // when
        ResultActions resultActions = mockMvc.perform(
                patch("/api/v1/members/inventoryProducts")
                        .header(HttpHeaders.AUTHORIZATION, authorizationHeader)
                        .content(objectMapper.writeValueAsString(profileProductRequest))
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
        );

        // then
        resultActions.andExpect(status().isOk())
                .andDo(print())
                .andDo(
                        document("member-inventoryProduct-update")
                );
    }
}
