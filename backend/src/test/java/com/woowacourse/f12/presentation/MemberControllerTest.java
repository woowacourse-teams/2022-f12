package com.woowacourse.f12.presentation;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.BDDMockito.willThrow;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.woowacourse.f12.application.JwtProvider;
import com.woowacourse.f12.application.MemberService;
import com.woowacourse.f12.dto.request.ProfileProductRequest;
import com.woowacourse.f12.exception.InventoryItemNotFoundException;
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
    void 대표_장비_등록_성공() throws Exception {
        // given
        String authorizationHeader = "Bearer Token";
        given(jwtProvider.validateToken(authorizationHeader))
                .willReturn(true);
        given(jwtProvider.getPayload(authorizationHeader))
                .willReturn("1");
        ProfileProductRequest profileProductRequest = new ProfileProductRequest(1L, 2L);
        willDoNothing().given(memberService).updateProfileProducts(1L, profileProductRequest);

        // when
        mockMvc.perform(
                        patch("/api/v1/members/inventoryProducts")
                                .header(HttpHeaders.AUTHORIZATION, authorizationHeader)
                                .content(objectMapper.writeValueAsString(profileProductRequest))
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                )
                .andExpect(status().isOk())
                .andDo(print());

        // then
        assertAll(
                () -> verify(jwtProvider).validateToken(authorizationHeader),
                () -> verify(jwtProvider).getPayload(authorizationHeader),
                () -> verify(memberService).updateProfileProducts(anyLong(), any(ProfileProductRequest.class))
        );
    }

    @Test
    void 대표_장비_등록_실패_인벤토리_상품_id가_없는_경우() throws Exception {
        // given
        String authorizationHeader = "Bearer Token";
        given(jwtProvider.validateToken(authorizationHeader))
                .willReturn(true);
        given(jwtProvider.getPayload(authorizationHeader))
                .willReturn("1");
        ProfileProductRequest profileProductRequest = new ProfileProductRequest(1L, 2L);
        willThrow(new InventoryItemNotFoundException()).given(memberService)
                .updateProfileProducts(anyLong(), any(ProfileProductRequest.class));

        // when
        mockMvc.perform(
                        patch("/api/v1/members/inventoryProducts")
                                .header(HttpHeaders.AUTHORIZATION, authorizationHeader)
                                .content(objectMapper.writeValueAsString(profileProductRequest))
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                )
                .andExpect(status().isNotFound())
                .andDo(print());

        // then
        assertAll(
                () -> verify(jwtProvider).validateToken(authorizationHeader),
                () -> verify(jwtProvider).getPayload(authorizationHeader),
                () -> verify(memberService).updateProfileProducts(anyLong(), any(ProfileProductRequest.class))
        );
    }
}
