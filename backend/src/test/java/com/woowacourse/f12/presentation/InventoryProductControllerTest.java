package com.woowacourse.f12.presentation;

import static com.woowacourse.f12.support.InventoryProductFixtures.SELECTED_INVENTORY_PRODUCT;
import static com.woowacourse.f12.support.KeyboardFixtures.KEYBOARD_1;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.BDDMockito.willThrow;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.woowacourse.f12.application.InventoryProductService;
import com.woowacourse.f12.application.JwtProvider;
import com.woowacourse.f12.domain.InventoryProduct;
import com.woowacourse.f12.dto.request.ProfileProductRequest;
import com.woowacourse.f12.dto.response.InventoryProductsResponse;
import com.woowacourse.f12.exception.InvalidProfileProductException;
import com.woowacourse.f12.exception.InventoryItemNotFoundException;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(InventoryProductController.class)
class InventoryProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private InventoryProductService inventoryProductService;

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
        willDoNothing().given(inventoryProductService).updateProfileProducts(1L, profileProductRequest);

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
                () -> verify(inventoryProductService).updateProfileProducts(anyLong(), any(ProfileProductRequest.class))
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
        willThrow(new InventoryItemNotFoundException()).given(inventoryProductService)
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
                () -> verify(inventoryProductService).updateProfileProducts(anyLong(), any(ProfileProductRequest.class))
        );
    }

    @Test
    void 대표_장비_등록_실패_요청된_장비가_모두_null인_경우() throws Exception {
        // given
        String authorizationHeader = "Bearer Token";
        given(jwtProvider.validateToken(authorizationHeader))
                .willReturn(true);
        given(jwtProvider.getPayload(authorizationHeader))
                .willReturn("1");
        ProfileProductRequest profileProductRequest = new ProfileProductRequest(null, null);
        willThrow(new InvalidProfileProductException()).given(inventoryProductService)
                .updateProfileProducts(anyLong(), any(ProfileProductRequest.class));

        // when
        mockMvc.perform(
                        patch("/api/v1/members/inventoryProducts")
                                .header(HttpHeaders.AUTHORIZATION, authorizationHeader)
                                .content(objectMapper.writeValueAsString(profileProductRequest))
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                )
                .andExpect(status().isBadRequest())
                .andDo(print());

        // then
        assertAll(
                () -> verify(jwtProvider).validateToken(authorizationHeader),
                () -> verify(jwtProvider).getPayload(authorizationHeader),
                () -> verify(inventoryProductService).updateProfileProducts(anyLong(), any(ProfileProductRequest.class))
        );
    }

    @Test
    void 멤버_id_로_조회한다() throws Exception {
        // given
        Long memberId = 1L;
        InventoryProduct inventoryProduct = SELECTED_INVENTORY_PRODUCT.생성(1L, memberId, KEYBOARD_1.생성(1L));
        String authorizationHeader = "Bearer Token";
        given(jwtProvider.validateToken(authorizationHeader))
                .willReturn(true);
        given(jwtProvider.getPayload(authorizationHeader))
                .willReturn("1");
        given(inventoryProductService.findByMemberId(memberId))
                .willReturn(InventoryProductsResponse.from(List.of(inventoryProduct)));

        // when
        mockMvc.perform(
                        get("/api/v1/members/inventoryProducts")
                                .header(HttpHeaders.AUTHORIZATION, authorizationHeader)

                )
                .andExpect(status().isOk())
                .andDo(print());

        // then
        assertAll(
                () -> verify(jwtProvider).validateToken(authorizationHeader),
                () -> verify(jwtProvider).getPayload(authorizationHeader),
                () -> verify(inventoryProductService).findByMemberId(memberId)
        );
    }

    @Test
    void 다른_멤버_id_로_조회한다() throws Exception {
        // given
        Long memberId = 1L;
        InventoryProduct inventoryProduct = SELECTED_INVENTORY_PRODUCT.생성(1L, memberId, KEYBOARD_1.생성(1L));
        given(inventoryProductService.findByMemberId(memberId))
                .willReturn(InventoryProductsResponse.from(List.of(inventoryProduct)));

        // when
        mockMvc.perform(
                        get("/api/v1/members/" + memberId + "/inventoryProducts")
                )
                .andExpect(status().isOk())
                .andDo(print());

        // then
        assertAll(
                () -> verify(inventoryProductService).findByMemberId(memberId)
        );
    }
}
