package com.woowacourse.f12.presentation.auth;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.woowacourse.f12.application.auth.JwtProvider;
import com.woowacourse.f12.application.review.ReviewService;
import com.woowacourse.f12.dto.request.review.ReviewRequest;
import com.woowacourse.f12.presentation.review.ReviewController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(ReviewController.class)
class AuthInterceptorTest {

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
    void 인증_인가를_검즘_실패_유효하지_않은_토큰() throws Exception {
        // given
        ReviewRequest reviewRequest = new ReviewRequest("content", 5);
        String authorizationHeader = "Bearer Token";
        given(jwtProvider.validateToken(authorizationHeader))
                .willReturn(false);
        given(reviewService.saveReviewAndInventoryProduct(anyLong(), anyLong(), any(ReviewRequest.class)))
                .willReturn(1L);
        // when
        mockMvc.perform(
                        post("/api/v1/keyboards/" + 1L + "/reviews")
                                .header(HttpHeaders.AUTHORIZATION, authorizationHeader)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(reviewRequest))
                ).andExpect(status().isUnauthorized())
                .andDo(print());

        // then
        assertAll(
                () -> verify(jwtProvider).validateToken(authorizationHeader),
                () -> verify(reviewService, times(0)).saveReviewAndInventoryProduct(eq(1L), eq(1L),
                        any(ReviewRequest.class))
        );
    }
}
