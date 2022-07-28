package com.woowacourse.f12.presentation.member;

import static com.woowacourse.f12.dto.CareerLevelConstant.JUNIOR_CONSTANT;
import static com.woowacourse.f12.dto.CareerLevelConstant.NONE_CONSTANT;
import static com.woowacourse.f12.dto.JobTypeConstant.BACKEND_CONSTANT;
import static com.woowacourse.f12.dto.JobTypeConstant.ETC_CONSTANT;
import static com.woowacourse.f12.support.InventoryProductFixtures.SELECTED_INVENTORY_PRODUCT;
import static com.woowacourse.f12.support.KeyboardFixtures.KEYBOARD_1;
import static com.woowacourse.f12.support.MemberFixtures.CORINNE;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.refEq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.woowacourse.f12.application.auth.JwtProvider;
import com.woowacourse.f12.application.member.MemberService;
import com.woowacourse.f12.domain.inventoryproduct.InventoryProduct;
import com.woowacourse.f12.domain.member.Member;
import com.woowacourse.f12.dto.request.member.MemberRequest;
import com.woowacourse.f12.dto.request.member.MemberSearchRequest;
import com.woowacourse.f12.dto.response.member.MemberPageResponse;
import com.woowacourse.f12.dto.response.member.MemberResponse;
import com.woowacourse.f12.exception.notfound.MemberNotFoundException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.SliceImpl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(MemberController.class)
@Import({CareerLevelParamConverter.class, JobTypeParamConverter.class})
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
        MemberRequest memberRequest = new MemberRequest(JUNIOR_CONSTANT, ETC_CONSTANT);
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
        memberRequest.put("careerLevel", "senior");
        memberRequest.put("jobType", "backend");

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

    @Test
    void 로그인된_상태에서_나의_회원정보를_수정_실패_Enum의_name값과_다른_요청일_경우() throws Exception {
        // given
        Map<String, Object> memberRequest = new HashMap<>();
        memberRequest.put("careerLevel", "invalid");
        memberRequest.put("jobType", "invalid");

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
                .andExpect(status().isBadRequest())
                .andDo(print());

        // then
        assertAll(
                () -> verify(jwtProvider).validateToken(authorizationHeader),
                () -> verify(jwtProvider).getPayload(authorizationHeader),
                () -> verify(memberService, times(0)).updateMember(eq(1L), any(MemberRequest.class))
        );
    }

    @Test
    void 로그인된_상태에서_나의_회원정보를_수정_실패_DTO_필드가_null인_경우() throws Exception {
        // given
        MemberRequest memberRequest = new MemberRequest(null, null);
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
                .andExpect(status().isBadRequest())
                .andDo(print());

        // then
        assertAll(
                () -> verify(jwtProvider).validateToken(authorizationHeader),
                () -> verify(jwtProvider).getPayload(authorizationHeader),
                () -> verify(memberService, times(0)).updateMember(eq(1L), any(MemberRequest.class))
        );
    }

    @Test
    void 키워드와_옵션으로_회원을_조회한다() throws Exception {
        // given
        MemberSearchRequest memberSearchRequest = new MemberSearchRequest("cheese", NONE_CONSTANT, BACKEND_CONSTANT);
        Pageable pageable = PageRequest.of(0, 10);
        InventoryProduct inventoryProduct = SELECTED_INVENTORY_PRODUCT.생성(CORINNE.생성(1L),
                KEYBOARD_1.생성(1L));
        Member member = CORINNE.대표장비를_추가해서_생성(1L, inventoryProduct);

        MemberPageResponse memberPageResponse = MemberPageResponse.from(
                new SliceImpl<>(List.of(member), pageable, false));
        given(memberService.findByContains(any(MemberSearchRequest.class), any(PageRequest.class)))
                .willReturn(memberPageResponse);

        // when
        mockMvc.perform(
                        get("/api/v1/members?query=cheese&careerLevel=none&jobType=backend&page=0&size=10")
                ).andExpect(status().isOk())
                .andDo(print());

        // then
        verify(memberService).findByContains(refEq(memberSearchRequest), refEq(pageable));
    }

    @Test
    void 회원_조회_실패_옵션값이_올바르지_않을때() throws Exception {
        // when
        mockMvc.perform(
                        get("/api/v1/members?query=cheese&careerLevel=invalid&jobType=invalid&page=0&size=10")
                ).andExpect(status().isBadRequest())
                .andDo(print());

        // then
        verify(memberService, times(0)).findByContains(any(MemberSearchRequest.class), any(PageRequest.class));
    }

    @Test
    void 회원_조회_성공_키워드와_옵션값이_주어지지_않을때() throws Exception {
        // given
        MemberSearchRequest memberSearchRequest = new MemberSearchRequest(null, null, null);
        Pageable pageable = PageRequest.of(0, 10);
        InventoryProduct inventoryProduct = SELECTED_INVENTORY_PRODUCT.생성(CORINNE.생성(1L),
                KEYBOARD_1.생성(1L));
        Member member = CORINNE.대표장비를_추가해서_생성(1L, inventoryProduct);

        MemberPageResponse memberPageResponse = MemberPageResponse.from(
                new SliceImpl<>(List.of(member), pageable, false));
        given(memberService.findByContains(any(MemberSearchRequest.class), any(PageRequest.class)))
                .willReturn(memberPageResponse);

        // when
        mockMvc.perform(
                        get("/api/v1/members?page=0&size=10")
                ).andExpect(status().isOk())
                .andDo(print());

        // then
        verify(memberService).findByContains(refEq(memberSearchRequest), refEq(pageable));
    }
}
