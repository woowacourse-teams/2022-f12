package com.woowacourse.f12.documentation;

import static com.woowacourse.f12.support.KeyboardFixtures.KEYBOARD_1;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.woowacourse.f12.application.InventoryProductService;
import com.woowacourse.f12.application.JwtProvider;
import com.woowacourse.f12.domain.InventoryProduct;
import com.woowacourse.f12.dto.response.InventoryProductsResponse;
import com.woowacourse.f12.presentation.InventoryProductController;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

@WebMvcTest(InventoryProductController.class)
class InventoryDocumentation extends Documentation {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private InventoryProductService inventoryProductService;

    @MockBean
    private JwtProvider jwtProvider;

    @Test
    void 멤버_id_로_인벤토리_상품_조회하는_API_문서화() throws Exception {
        // given
        Long memberId = 1L;
        InventoryProduct inventoryProduct = InventoryProduct.builder()
                .id(1L)
                .memberId(memberId)
                .keyboard(KEYBOARD_1.생성(1L))
                .isSelected(true)
                .build();
        String authorizationHeader = "Bearer Token";
        given(jwtProvider.validateToken(authorizationHeader))
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
                .andDo(document("inventoryProducts-get-by-memberId"));
    }
}
