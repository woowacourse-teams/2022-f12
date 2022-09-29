package com.woowacourse.f12.presentation.auth;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.woowacourse.f12.application.auth.JwtProvider;
import com.woowacourse.f12.application.review.ReviewService;
import com.woowacourse.f12.dto.request.review.ReviewRequest;
import com.woowacourse.f12.presentation.PresentationTest;
import com.woowacourse.f12.presentation.review.ReviewController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(ReviewController.class)
class AuthInterceptorTest extends PresentationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ReviewService reviewService;

    @MockBean
    private JwtProvider jwtProvider;

    private final ObjectMapper objectMapper;

    public AuthInterceptorTest() {
        this.objectMapper = new ObjectMapper();
    }

    @Test
    void 인증_인가를_검증_실패_유효하지_않은_액세스_토큰() throws Exception {
        // given
        ReviewRequest reviewRequest = new ReviewRequest("content", 5);
        String authorizationHeader = "Bearer Token";
        given(jwtProvider.isValidToken(authorizationHeader))
                .willReturn(false);
        given(reviewService.saveReviewAndInventoryProduct(anyLong(), anyLong(), any(ReviewRequest.class)))
                .willReturn(1L);

        // when
        mockMvc.perform(
                        post("/api/v1/products/" + 1L + "/reviews")
                                .header(HttpHeaders.AUTHORIZATION, authorizationHeader)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(reviewRequest))
                ).andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.errorCode").value("40101"))
                .andDo(print());

        // then
        assertAll(
                () -> verify(jwtProvider).isValidToken(authorizationHeader),
                () -> verify(reviewService, times(0)).saveReviewAndInventoryProduct(eq(1L), eq(1L),
                        any(ReviewRequest.class))
        );
    }

    @Test
    void 인증_인가가_필수가_아닌_경우에_만료된_액세스_토큰으로_요청보내면_예외발생() throws Exception {
        // given
        String authorizationHeader = "Bearer InvalidToken";
        given(jwtProvider.isValidToken(authorizationHeader))
                .willReturn(false);
        given(reviewService.findPageByProductId(anyLong(), anyLong(), any()))
                .willReturn(null);

        // when
        mockMvc.perform(
                        get("/api/v1/products/" + 1L + "/reviews")
                                .header(HttpHeaders.AUTHORIZATION, authorizationHeader)
                ).andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.errorCode").value("40101"))
                .andDo(print());

        // then
        assertAll(
                () -> verify(jwtProvider).isValidToken(authorizationHeader),
                () -> verify(reviewService, times(0)).findPageByProductId(any(), any(), any())
        );
    }

    @Test
    void 인증_인가가_필수인_경우에_토큰이_없는_경우_예외_발생() throws Exception {
        // given
        given(jwtProvider.isValidToken(any()))
                .willReturn(false);
        willDoNothing().given(reviewService).delete(anyLong(), anyLong());

        // when
        mockMvc.perform(
                        delete("/api/v1/reviews/" + 1L)
                ).andExpect(status().isUnauthorized())
                .andDo(print());

        // then
        assertAll(
                () -> verify(jwtProvider, times(0)).isValidToken(any()),
                () -> verify(reviewService, times(0)).delete(any(), any())
        );
    }
}
