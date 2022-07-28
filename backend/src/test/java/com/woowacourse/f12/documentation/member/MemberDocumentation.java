package com.woowacourse.f12.documentation.member;

import static com.woowacourse.f12.support.InventoryProductFixtures.SELECTED_INVENTORY_PRODUCT;
import static com.woowacourse.f12.support.MemberFixtures.CORINNE;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.woowacourse.f12.application.auth.JwtProvider;
import com.woowacourse.f12.application.member.MemberService;
import com.woowacourse.f12.documentation.Documentation;
import com.woowacourse.f12.domain.inventoryproduct.InventoryProduct;
import com.woowacourse.f12.domain.member.Member;
import com.woowacourse.f12.dto.CareerLevelConstant;
import com.woowacourse.f12.dto.JobTypeConstant;
import com.woowacourse.f12.dto.request.member.MemberRequest;
import com.woowacourse.f12.dto.request.member.MemberSearchRequest;
import com.woowacourse.f12.dto.response.member.MemberPageResponse;
import com.woowacourse.f12.dto.response.member.MemberResponse;
import com.woowacourse.f12.presentation.member.MemberController;
import com.woowacourse.f12.support.KeyboardFixtures;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.SliceImpl;
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
        MemberRequest memberRequest = new MemberRequest(CareerLevelConstant.JUNIOR, JobTypeConstant.BACKEND);
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

    @Test
    void 키워드와_옵션으로_회원을_조회_API_문서화() throws Exception {
        // given
        Pageable pageable = PageRequest.of(0, 10);
        InventoryProduct inventoryProduct = SELECTED_INVENTORY_PRODUCT.생성(CORINNE.생성(1L),
                KeyboardFixtures.KEYBOARD_1.생성(1L));
        Member member = CORINNE.대표장비_추가(1L, inventoryProduct);

        MemberPageResponse memberPageResponse = MemberPageResponse.from(
                new SliceImpl<>(List.of(member), pageable, false));
        given(memberService.findByContains(any(MemberSearchRequest.class), any(PageRequest.class)))
                .willReturn(memberPageResponse);

        // when, then
        mockMvc.perform(
                        get("/api/v1/members?query=cheese&careerLevel=none&jobType=backend&page=0&size=10")
                ).andExpect(status().isOk())
                .andDo(document("members-search"))
                .andDo(print());
    }
}
