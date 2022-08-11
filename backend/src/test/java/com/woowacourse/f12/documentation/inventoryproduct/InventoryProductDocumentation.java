package com.woowacourse.f12.documentation.inventoryproduct;

import static com.woowacourse.f12.support.InventoryProductFixtures.SELECTED_INVENTORY_PRODUCT;
import static com.woowacourse.f12.support.MemberFixtures.CORINNE;
import static com.woowacourse.f12.support.ProductFixture.KEYBOARD_1;
import static com.woowacourse.f12.support.ProductFixture.MOUSE_1;
import static com.woowacourse.f12.support.ReviewFixtures.REVIEW_RATING_1;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.woowacourse.f12.application.auth.JwtProvider;
import com.woowacourse.f12.application.inventoryproduct.InventoryProductService;
import com.woowacourse.f12.documentation.Documentation;
import com.woowacourse.f12.domain.inventoryproduct.InventoryProduct;
import com.woowacourse.f12.domain.member.Member;
import com.woowacourse.f12.dto.request.inventoryproduct.ProfileProductRequest;
import com.woowacourse.f12.dto.response.inventoryproduct.InventoryProductsResponse;
import com.woowacourse.f12.dto.response.review.ReviewResponse;
import com.woowacourse.f12.presentation.inventoryproduct.InventoryProductController;
import com.woowacourse.f12.support.MemberFixtures;
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
class InventoryProductDocumentation extends Documentation {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private InventoryProductService inventoryProductService;

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
        ProfileProductRequest profileProductRequest = new ProfileProductRequest(List.of(1L));
        willDoNothing().given(inventoryProductService).updateProfileProducts(memberId, profileProductRequest);

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
    }

    @Test
    void 멤버_id_로_인벤토리_상품_조회하는_API_문서화() throws Exception {
        // given
        Long memberId = 1L;
        Member member = MemberFixtures.CORINNE.생성(memberId);
        InventoryProduct inventoryProduct1 = SELECTED_INVENTORY_PRODUCT.생성(1L, member, KEYBOARD_1.생성(1L));
        InventoryProduct inventoryProduct2 = SELECTED_INVENTORY_PRODUCT.생성(2L, member, MOUSE_1.생성(1L));
        String authorizationHeader = "Bearer Token";
        given(jwtProvider.validateToken(authorizationHeader))
                .willReturn(true);
        given(jwtProvider.getPayload(authorizationHeader))
                .willReturn("1");
        given(inventoryProductService.findByMemberId(memberId))
                .willReturn(InventoryProductsResponse.from(List.of(inventoryProduct1, inventoryProduct2)));

        // when
        ResultActions resultActions = mockMvc.perform(
                get("/api/v1/members/inventoryProducts")
                        .header(HttpHeaders.AUTHORIZATION, authorizationHeader)
        );

        // then
        resultActions.andExpect(status().isOk())
                .andDo(print())
                .andDo(document("inventoryProducts-get-own"));
    }

    @Test
    void 다른_멤버_id_로_인벤토리_상품_조회하는_API_문서화() throws Exception {
        // given
        Long memberId = 1L;
        Member member = MemberFixtures.CORINNE.생성(memberId);
        InventoryProduct inventoryProduct1 = SELECTED_INVENTORY_PRODUCT.생성(1L, member, KEYBOARD_1.생성(1L));
        InventoryProduct inventoryProduct2 = SELECTED_INVENTORY_PRODUCT.생성(2L, member, MOUSE_1.생성(1L));
        given(inventoryProductService.findByMemberId(memberId))
                .willReturn(InventoryProductsResponse.from(List.of(inventoryProduct1, inventoryProduct2)));

        // when
        ResultActions resultActions = mockMvc.perform(
                get("/api/v1/members/" + memberId + "/inventoryProducts")
        );

        // then
        resultActions.andExpect(status().isOk())
                .andDo(print())
                .andDo(document("inventoryProducts-get-by-memberId"));
    }

    @Test
    void 인벤토리_아이디로_리뷰를_조회하는_API_문서화() throws Exception {
        // given
        Long inventoryId = 1L;
        given(inventoryProductService.findReviewById(inventoryId))
                .willReturn(ReviewResponse.from(REVIEW_RATING_1.작성(1L, KEYBOARD_1.생성(1L), CORINNE.생성(1L))));

        // when
        ResultActions resultActions = mockMvc.perform(
                get("/api/v1/inventoryProducts/" + inventoryId + "/reviews")
        );

        // then
        resultActions.andExpect(status().isOk())
                .andDo(print())
                .andDo(document("reviews-get-by-inventoryProductId"));
    }
}
