package com.woowacourse.f12.presentation.inventoryproduct;

import static com.woowacourse.f12.exception.ErrorCode.CATEGORY_DUPLICATED_PROFILE_PRODUCT;
import static com.woowacourse.f12.exception.ErrorCode.EXPIRED_ACCESS_TOKEN;
import static com.woowacourse.f12.exception.ErrorCode.INVALID_CATEGORY_PROFILE_PRODUCT;
import static com.woowacourse.f12.exception.ErrorCode.INVALID_REQUEST_BODY_TYPE;
import static com.woowacourse.f12.exception.ErrorCode.INVALID_TOKEN_FORMAT;
import static com.woowacourse.f12.exception.ErrorCode.INVENTORY_PRODUCT_NOT_FOUND;
import static com.woowacourse.f12.exception.ErrorCode.MEMBER_NOT_FOUND;
import static com.woowacourse.f12.exception.ErrorCode.NOT_EXIST_ACCESS_TOKEN;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.woowacourse.f12.application.auth.token.JwtProvider;
import com.woowacourse.f12.application.auth.token.MemberPayload;
import com.woowacourse.f12.application.inventoryproduct.InventoryProductService;
import com.woowacourse.f12.domain.inventoryproduct.InventoryProduct;
import com.woowacourse.f12.domain.member.Member;
import com.woowacourse.f12.domain.member.Role;
import com.woowacourse.f12.dto.request.inventoryproduct.ProfileProductRequest;
import com.woowacourse.f12.dto.response.inventoryproduct.InventoryProductsResponse;
import com.woowacourse.f12.exception.badrequest.DuplicatedProfileProductCategoryException;
import com.woowacourse.f12.exception.badrequest.InvalidProfileProductCategoryException;
import com.woowacourse.f12.exception.notfound.InventoryProductNotFoundException;
import com.woowacourse.f12.exception.notfound.MemberNotFoundException;
import com.woowacourse.f12.presentation.PresentationTest;
import com.woowacourse.f12.support.ErrorCodeSnippet;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

class InventoryProductControllerTest extends PresentationTest {

    @Autowired
    private MockMvc mockMvc;
    
    @Autowired
    private JwtProvider jwtProvider;

    @Autowired
    private InventoryProductService inventoryProductService;

    @Test
    void 대표_장비_등록_성공() throws Exception {
        // given
        String authorizationHeader = "Bearer Token";
        given(jwtProvider.isValidToken(authorizationHeader))
                .willReturn(true);
        given(jwtProvider.getPayload(authorizationHeader))
                .willReturn(new MemberPayload(1L, Role.USER));
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
                        document("inventoryProducts-update",
                                new ErrorCodeSnippet(NOT_EXIST_ACCESS_TOKEN, EXPIRED_ACCESS_TOKEN, INVALID_TOKEN_FORMAT,
                                        INVENTORY_PRODUCT_NOT_FOUND, INVALID_REQUEST_BODY_TYPE,
                                        CATEGORY_DUPLICATED_PROFILE_PRODUCT, INVALID_CATEGORY_PROFILE_PRODUCT))
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
                .willReturn(new MemberPayload(1L, Role.USER));
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
                .andExpect(jsonPath("$.errorCode").value(INVENTORY_PRODUCT_NOT_FOUND.getValue()))
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
                .andExpect(jsonPath("$.errorCode").value(INVALID_REQUEST_BODY_TYPE.getValue()))
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
                .willReturn(new MemberPayload(1L, Role.USER));
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
                .andExpect(jsonPath("$.errorCode").value(CATEGORY_DUPLICATED_PROFILE_PRODUCT.getValue()))
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
                .willReturn(new MemberPayload(1L, Role.USER));
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
                .andExpect(jsonPath("$.errorCode").value(INVALID_CATEGORY_PROFILE_PRODUCT.getValue()))
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
                .willReturn(new MemberPayload(1L, Role.USER));
        given(inventoryProductService.findByMemberId(memberId))
                .willReturn(InventoryProductsResponse.from(List.of(inventoryProduct)));

        // when
        ResultActions resultActions = mockMvc.perform(
                get("/api/v1/members/inventoryProducts")
                        .header(HttpHeaders.AUTHORIZATION, authorizationHeader)
        );

        // then
        resultActions.andExpect(status().isOk())
                .andDo(document("inventoryProducts-get-own",
                        new ErrorCodeSnippet(NOT_EXIST_ACCESS_TOKEN, EXPIRED_ACCESS_TOKEN, INVALID_TOKEN_FORMAT,
                                MEMBER_NOT_FOUND)))
                .andDo(print());

        assertAll(
                () -> verify(jwtProvider).isValidToken(authorizationHeader),
                () -> verify(jwtProvider).getPayload(authorizationHeader),
                () -> verify(inventoryProductService).findByMemberId(memberId)
        );
    }

    @Test
    void 존재하지_않는_멤버_id로_조회할_경우_예외가_발생한다() throws Exception {
        // given
        String authorizationHeader = "Bearer Token";
        given(jwtProvider.isValidToken(authorizationHeader))
                .willReturn(true);
        given(jwtProvider.getPayload(authorizationHeader))
                .willReturn(new MemberPayload(1L, Role.USER));
        given(inventoryProductService.findByMemberId(1L))
                .willThrow(new MemberNotFoundException());

        // when
        ResultActions resultActions = mockMvc.perform(
                get("/api/v1/members/inventoryProducts")
                        .header(HttpHeaders.AUTHORIZATION, authorizationHeader)
        );

        // then
        resultActions.andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errorCode").value(MEMBER_NOT_FOUND.getValue()))
                .andDo(print());
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
                .andDo(document("inventoryProducts-get-by-memberId",
                        new ErrorCodeSnippet(MEMBER_NOT_FOUND)));

        assertAll(
                () -> verify(inventoryProductService).findByMemberId(memberId)
        );
    }
}
