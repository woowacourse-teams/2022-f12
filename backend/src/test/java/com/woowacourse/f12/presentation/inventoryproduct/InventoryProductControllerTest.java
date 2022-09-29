package com.woowacourse.f12.presentation.inventoryproduct;

import static com.woowacourse.f12.support.fixture.InventoryProductFixtures.SELECTED_INVENTORY_PRODUCT;
import static com.woowacourse.f12.support.fixture.MemberFixture.CORINNE;
import static com.woowacourse.f12.support.fixture.ProductFixture.KEYBOARD_1;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.BDDMockito.willThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.woowacourse.f12.application.auth.JwtProvider;
import com.woowacourse.f12.application.inventoryproduct.InventoryProductService;
import com.woowacourse.f12.domain.inventoryproduct.InventoryProduct;
import com.woowacourse.f12.domain.member.Member;
import com.woowacourse.f12.dto.request.inventoryproduct.ProfileProductRequest;
import com.woowacourse.f12.dto.response.inventoryproduct.InventoryProductsResponse;
import com.woowacourse.f12.exception.badrequest.DuplicatedProfileProductCategoryException;
import com.woowacourse.f12.exception.badrequest.InvalidProfileProductCategoryException;
import com.woowacourse.f12.exception.notfound.InventoryProductNotFoundException;
import com.woowacourse.f12.presentation.PresentationTest;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

@WebMvcTest(InventoryProductController.class)
class InventoryProductControllerTest extends PresentationTest {

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
        given(jwtProvider.isValidToken(authorizationHeader))
                .willReturn(true);
        given(jwtProvider.getPayload(authorizationHeader))
                .willReturn("1");
        ProfileProductRequest profileProductRequest = new ProfileProductRequest(List.of(1L));
        willDoNothing().given(inventoryProductService).updateProfileProducts(1L, profileProductRequest);

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
                        document("inventoryProducts-update")
                );

        assertAll(
                () -> verify(jwtProvider).isValidToken(authorizationHeader),
                () -> verify(jwtProvider).getPayload(authorizationHeader),
                () -> verify(inventoryProductService).updateProfileProducts(anyLong(), any(ProfileProductRequest.class))
        );
    }

    @Test
    void 대표_장비_등록_실패_인벤토리_상품_id가_없는_경우() throws Exception {
        // given
        String authorizationHeader = "Bearer Token";
        given(jwtProvider.isValidToken(authorizationHeader))
                .willReturn(true);
        given(jwtProvider.getPayload(authorizationHeader))
                .willReturn("1");
        ProfileProductRequest profileProductRequest = new ProfileProductRequest(List.of(1L));
        willThrow(new InventoryProductNotFoundException()).given(inventoryProductService)
                .updateProfileProducts(anyLong(), any(ProfileProductRequest.class));

        // when
        ResultActions resultActions = mockMvc.perform(
                patch("/api/v1/members/inventoryProducts")
                        .header(HttpHeaders.AUTHORIZATION, authorizationHeader)
                        .content(objectMapper.writeValueAsString(profileProductRequest))
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
        );

        // then
        resultActions.andExpect(status().isNotFound())
                .andDo(print());

        assertAll(
                () -> verify(jwtProvider).isValidToken(authorizationHeader),
                () -> verify(jwtProvider).getPayload(authorizationHeader),
                () -> verify(inventoryProductService).updateProfileProducts(anyLong(), any(ProfileProductRequest.class))
        );
    }

    @Test
    void 대표_장비_등록_실패_요청된_장비가_모두_null인_경우() throws Exception {
        // given
        String authorizationHeader = "Bearer Token";
        given(jwtProvider.isValidToken(authorizationHeader))
                .willReturn(true);
        ProfileProductRequest profileProductRequest = new ProfileProductRequest(null);

        // when
        ResultActions resultActions = mockMvc.perform(
                patch("/api/v1/members/inventoryProducts")
                        .header(HttpHeaders.AUTHORIZATION, authorizationHeader)
                        .content(objectMapper.writeValueAsString(profileProductRequest))
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
        );

        // then
        resultActions.andExpect(status().isBadRequest())
                .andDo(print());

        assertAll(
                () -> verify(jwtProvider).isValidToken(authorizationHeader),
                () -> verify(jwtProvider, times(0)).getPayload(authorizationHeader),
                () -> verify(inventoryProductService, times(0)).updateProfileProducts(anyLong(),
                        any(ProfileProductRequest.class))
        );
    }

    @Test
    void 대표_장비_등록_실패_요청된_장비가_중복된_카테고리인_경우() throws Exception {
        // given
        String authorizationHeader = "Bearer Token";
        given(jwtProvider.isValidToken(authorizationHeader))
                .willReturn(true);
        given(jwtProvider.getPayload(authorizationHeader))
                .willReturn("1");
        ProfileProductRequest profileProductRequest = new ProfileProductRequest(List.of(1L, 2L));
        willThrow(new DuplicatedProfileProductCategoryException())
                .given(inventoryProductService).updateProfileProducts(anyLong(), any(ProfileProductRequest.class));

        // when
        ResultActions resultActions = mockMvc.perform(
                patch("/api/v1/members/inventoryProducts")
                        .header(HttpHeaders.AUTHORIZATION, authorizationHeader)
                        .content(objectMapper.writeValueAsString(profileProductRequest))
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
        );

        // then
        resultActions.andExpect(status().isBadRequest())
                .andDo(print());

        assertAll(
                () -> verify(jwtProvider).isValidToken(authorizationHeader),
                () -> verify(jwtProvider).getPayload(authorizationHeader),
                () -> verify(inventoryProductService).updateProfileProducts(anyLong(), any(ProfileProductRequest.class))
        );
    }

    @Test
    void 대표_장비_등록_실패_요청된_장비에_소프트웨어_카테고리가_포함된_경우() throws Exception {
        // given
        String authorizationHeader = "Bearer Token";
        given(jwtProvider.isValidToken(authorizationHeader))
                .willReturn(true);
        given(jwtProvider.getPayload(authorizationHeader))
                .willReturn("1");
        ProfileProductRequest profileProductRequest = new ProfileProductRequest(List.of(1L, 2L));
        willThrow(new InvalidProfileProductCategoryException())
                .given(inventoryProductService).updateProfileProducts(anyLong(), any(ProfileProductRequest.class));

        // when
        ResultActions resultActions = mockMvc.perform(
                patch("/api/v1/members/inventoryProducts")
                        .header(HttpHeaders.AUTHORIZATION, authorizationHeader)
                        .content(objectMapper.writeValueAsString(profileProductRequest))
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
        );

        // then
        resultActions.andExpect(status().isBadRequest())
                .andDo(print());

        assertAll(
                () -> verify(jwtProvider).isValidToken(authorizationHeader),
                () -> verify(jwtProvider).getPayload(authorizationHeader),
                () -> verify(inventoryProductService).updateProfileProducts(anyLong(), any(ProfileProductRequest.class))
        );
    }

    @Test
    void 멤버_id_로_조회한다() throws Exception {
        // given
        Long memberId = 1L;
        Member member = CORINNE.생성(memberId);
        InventoryProduct inventoryProduct = SELECTED_INVENTORY_PRODUCT.생성(1L, member, KEYBOARD_1.생성(1L));
        String authorizationHeader = "Bearer Token";
        given(jwtProvider.isValidToken(authorizationHeader))
                .willReturn(true);
        given(jwtProvider.getPayload(authorizationHeader))
                .willReturn("1");
        given(inventoryProductService.findByMemberId(memberId))
                .willReturn(InventoryProductsResponse.from(List.of(inventoryProduct)));

        // when
        ResultActions resultActions = mockMvc.perform(
                get("/api/v1/members/inventoryProducts")
                        .header(HttpHeaders.AUTHORIZATION, authorizationHeader)
        );

        // then
        resultActions.andExpect(status().isOk())
                .andDo(print())
                .andDo(document("inventoryProducts-get-own"));

        assertAll(
                () -> verify(jwtProvider).isValidToken(authorizationHeader),
                () -> verify(jwtProvider).getPayload(authorizationHeader),
                () -> verify(inventoryProductService).findByMemberId(memberId)
        );
    }

    @Test
    void 다른_멤버_id_로_조회한다() throws Exception {
        // given
        Long memberId = 1L;
        Member member = CORINNE.생성(memberId);
        InventoryProduct inventoryProduct = SELECTED_INVENTORY_PRODUCT.생성(1L, member, KEYBOARD_1.생성(1L));
        given(inventoryProductService.findByMemberId(memberId))
                .willReturn(InventoryProductsResponse.from(List.of(inventoryProduct)));

        // when
        ResultActions resultActions = mockMvc.perform(
                get("/api/v1/members/" + memberId + "/inventoryProducts")
        );

        // then
        resultActions.andExpect(status().isOk())
                .andDo(print())
                .andDo(document("inventoryProducts-get-by-memberId"));

        assertAll(
                () -> verify(inventoryProductService).findByMemberId(memberId)
        );
    }
}
