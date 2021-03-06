package com.woowacourse.f12.presentation;

import static com.woowacourse.f12.support.KeyboardFixtures.KEYBOARD_1;
import static com.woowacourse.f12.support.MemberFixtures.CORINNE;
import static com.woowacourse.f12.support.ReviewFixtures.REVIEW_RATING_5;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.BDDMockito.willThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.woowacourse.f12.application.JwtProvider;
import com.woowacourse.f12.application.ReviewService;
import com.woowacourse.f12.dto.request.ReviewRequest;
import com.woowacourse.f12.dto.response.ReviewPageResponse;
import com.woowacourse.f12.dto.response.ReviewWithProductPageResponse;
import com.woowacourse.f12.exception.BlankContentException;
import com.woowacourse.f12.exception.InvalidContentLengthException;
import com.woowacourse.f12.exception.InvalidRatingValueException;
import com.woowacourse.f12.exception.KeyboardNotFoundException;
import com.woowacourse.f12.exception.MemberNotFoundException;
import com.woowacourse.f12.exception.NotAuthorException;
import com.woowacourse.f12.exception.ReviewNotFoundException;
import com.woowacourse.f12.support.AuthTokenExtractor;
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
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(ReviewController.class)
@Import(AuthTokenExtractor.class)
class ReviewControllerTest {

    private static final long PRODUCT_ID = 1L;

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper;

    @MockBean
    private ReviewService reviewService;

    @MockBean
    private JwtProvider jwtProvider;

    public ReviewControllerTest() {
        this.objectMapper = new ObjectMapper();
    }

    @Test
    void ??????_??????_??????() throws Exception {
        // given
        ReviewRequest reviewRequest = new ReviewRequest("content", 5);
        String authorizationHeader = "Bearer Token";
        given(jwtProvider.validateToken(authorizationHeader))
                .willReturn(true);
        given(jwtProvider.getPayload(authorizationHeader))
                .willReturn("1");
        given(reviewService.saveReviewAndInventoryProduct(anyLong(), anyLong(), any(ReviewRequest.class)))
                .willReturn(1L);
        // when
        mockMvc.perform(
                        post("/api/v1/keyboards/" + PRODUCT_ID + "/reviews")
                                .header(HttpHeaders.AUTHORIZATION, authorizationHeader)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(reviewRequest))
                ).andExpect(status().isCreated())
                .andDo(print());

        // then
        assertAll(
                () -> verify(jwtProvider).validateToken(authorizationHeader),
                () -> verify(jwtProvider).getPayload(authorizationHeader),
                () -> verify(reviewService).saveReviewAndInventoryProduct(eq(PRODUCT_ID), eq(1L),
                        any(ReviewRequest.class))
        );
    }

    @Test
    void ??????_??????_??????_Request_DTO_???_?????????_null_???_??????() throws Exception {
        // given
        ReviewRequest reviewRequest = new ReviewRequest(null, null);
        String authorizationHeader = "Bearer Token";
        given(jwtProvider.validateToken(authorizationHeader))
                .willReturn(true);
        given(jwtProvider.getPayload(authorizationHeader))
                .willReturn("1");
        given(reviewService.saveReviewAndInventoryProduct(anyLong(), anyLong(), any(ReviewRequest.class)))
                .willThrow(new BlankContentException());

        // when
        mockMvc.perform(
                        post("/api/v1/keyboards/" + PRODUCT_ID + "/reviews")
                                .header(HttpHeaders.AUTHORIZATION, authorizationHeader)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(reviewRequest))
                ).andExpect(status().isBadRequest())
                .andDo(print());

        // then
        assertAll(
                () -> verify(jwtProvider).validateToken(authorizationHeader),
                () -> verify(reviewService, times(0)).saveReviewAndInventoryProduct(eq(PRODUCT_ID), eq(1L),
                        any(ReviewRequest.class))
        );
    }

    @Test
    void ??????_??????_??????_rating_?????????_????????????_??????_??????() throws Exception {
        // given
        String authorizationHeader = "Bearer Token";
        Map<String, Object> reviewRequest = new HashMap<>();
        reviewRequest.put("content", "??????");
        reviewRequest.put("rating", "??????");
        given(jwtProvider.validateToken(authorizationHeader))
                .willReturn(true);
        given(jwtProvider.getPayload(authorizationHeader))
                .willReturn("1");
        given(reviewService.saveReviewAndInventoryProduct(anyLong(), anyLong(), any(ReviewRequest.class)))
                .willThrow(new BlankContentException());

        // when
        mockMvc.perform(
                        post("/api/v1/keyboards/" + PRODUCT_ID + "/reviews")
                                .header(HttpHeaders.AUTHORIZATION, authorizationHeader)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(reviewRequest))
                ).andExpect(status().isBadRequest())
                .andDo(print());

        // then
        assertAll(
                () -> verify(jwtProvider).validateToken(authorizationHeader),
                () -> verify(reviewService, times(0)).saveReviewAndInventoryProduct(eq(PRODUCT_ID), eq(1L),
                        any(ReviewRequest.class))
        );
    }

    @Test
    void ??????_??????_??????_??????_?????????_????????????_??????() throws Exception {
        // given
        String authorizationHeader = "Bearer Token";
        ReviewRequest reviewRequest = new ReviewRequest("", 5);
        given(jwtProvider.validateToken(authorizationHeader))
                .willReturn(true);
        given(jwtProvider.getPayload(authorizationHeader))
                .willReturn("1");
        given(reviewService.saveReviewAndInventoryProduct(anyLong(), anyLong(), any(ReviewRequest.class)))
                .willThrow(new BlankContentException());

        // when
        mockMvc.perform(
                        post("/api/v1/keyboards/" + PRODUCT_ID + "/reviews")
                                .header(HttpHeaders.AUTHORIZATION, authorizationHeader)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(reviewRequest))
                ).andExpect(status().isBadRequest())
                .andDo(print());

        // then
        assertAll(
                () -> verify(jwtProvider).validateToken(authorizationHeader),
                () -> verify(jwtProvider).getPayload(authorizationHeader),
                () -> verify(reviewService).saveReviewAndInventoryProduct(eq(PRODUCT_ID), eq(1L),
                        any(ReviewRequest.class))
        );
    }

    @Test
    void ??????_??????_??????_??????_??????_??????_??????_??????() throws Exception {
        // given
        String authorizationHeader = "Bearer Token";
        String content = "a".repeat(1001);
        ReviewRequest reviewRequest = new ReviewRequest(content, 5);
        given(jwtProvider.validateToken(authorizationHeader))
                .willReturn(true);
        given(jwtProvider.getPayload(authorizationHeader))
                .willReturn("1");
        given(reviewService.saveReviewAndInventoryProduct(anyLong(), anyLong(), any(ReviewRequest.class)))
                .willThrow(new InvalidContentLengthException(1000));

        // when
        mockMvc.perform(
                        post("/api/v1/keyboards/" + PRODUCT_ID + "/reviews")
                                .header(HttpHeaders.AUTHORIZATION, authorizationHeader)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(reviewRequest))
                ).andExpect(status().isBadRequest())
                .andDo(print());

        // then
        assertAll(
                () -> verify(jwtProvider).validateToken(authorizationHeader),
                () -> verify(jwtProvider).getPayload(authorizationHeader),
                () -> verify(reviewService).saveReviewAndInventoryProduct(eq(PRODUCT_ID), eq(1L),
                        any(ReviewRequest.class))
        );
    }

    @Test
    void ??????_??????_??????_?????????_1??????_5??????_?????????_??????() throws Exception {
        // given
        String authorizationHeader = "Bearer Token";
        ReviewRequest reviewRequest = new ReviewRequest("??????", 0);
        given(jwtProvider.validateToken(authorizationHeader))
                .willReturn(true);
        given(jwtProvider.getPayload(authorizationHeader))
                .willReturn("1");
        given(reviewService.saveReviewAndInventoryProduct(anyLong(), anyLong(), any(ReviewRequest.class)))
                .willThrow(new InvalidRatingValueException());

        // when
        mockMvc.perform(
                        post("/api/v1/keyboards/" + PRODUCT_ID + "/reviews")
                                .header(HttpHeaders.AUTHORIZATION, authorizationHeader)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(reviewRequest))
                ).andExpect(status().isBadRequest())
                .andDo(print());

        // then
        assertAll(
                () -> verify(jwtProvider).validateToken(authorizationHeader),
                () -> verify(jwtProvider).getPayload(authorizationHeader),
                () -> verify(reviewService).saveReviewAndInventoryProduct(eq(PRODUCT_ID), eq(1L),
                        any(ReviewRequest.class))
        );
    }

    @Test
    void ??????_??????_??????_?????????_????????????_??????() throws Exception {
        // given
        String authorizationHeader = "Bearer Token";
        ReviewRequest reviewRequest = new ReviewRequest("??????", 5);
        given(jwtProvider.validateToken(authorizationHeader))
                .willReturn(true);
        given(jwtProvider.getPayload(authorizationHeader))
                .willReturn("1");
        given(reviewService.saveReviewAndInventoryProduct(anyLong(), anyLong(), any(ReviewRequest.class)))
                .willThrow(new KeyboardNotFoundException());

        // when
        mockMvc.perform(
                        post("/api/v1/keyboards/" + 0L + "/reviews")
                                .header(HttpHeaders.AUTHORIZATION, authorizationHeader)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(reviewRequest))
                ).andExpect(status().isNotFound())
                .andDo(print());

        // then
        assertAll(
                () -> verify(jwtProvider).validateToken(authorizationHeader),
                () -> verify(jwtProvider).getPayload(authorizationHeader),
                () -> verify(reviewService).saveReviewAndInventoryProduct(eq(0L), eq(1L), any(ReviewRequest.class))
        );
    }

    @Test
    void ??????_??????_??????_?????????_????????????_??????() throws Exception {
        // given
        String authorizationHeader = "Bearer Token";
        ReviewRequest reviewRequest = new ReviewRequest("??????", 5);
        given(jwtProvider.validateToken(authorizationHeader))
                .willReturn(true);
        given(jwtProvider.getPayload(authorizationHeader))
                .willReturn("1");
        given(reviewService.saveReviewAndInventoryProduct(anyLong(), anyLong(), any(ReviewRequest.class)))
                .willThrow(new MemberNotFoundException());

        // when
        mockMvc.perform(
                        post("/api/v1/keyboards/" + 1L + "/reviews")
                                .header(HttpHeaders.AUTHORIZATION, authorizationHeader)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(reviewRequest))
                ).andExpect(status().isNotFound())
                .andDo(print());

        // then
        assertAll(
                () -> verify(jwtProvider).validateToken(authorizationHeader),
                () -> verify(jwtProvider).getPayload(authorizationHeader),
                () -> verify(reviewService).saveReviewAndInventoryProduct(eq(1L), eq(1L), any(ReviewRequest.class))
        );
    }

    @Test
    void ??????_??????_?????????_??????_??????() throws Exception {
        // given
        given(reviewService.findPage(any(Pageable.class)))
                .willReturn(ReviewWithProductPageResponse.from(
                        new SliceImpl<>(List.of(REVIEW_RATING_5.??????(1L, KEYBOARD_1.??????(), CORINNE.??????(1L))))));

        // when
        mockMvc.perform(get("/api/v1/reviews?size=150&page=0&sort=rating,desc"))
                .andExpect(status().isOk())
                .andDo(print());

        // then
        verify(reviewService).findPage(PageRequest.of(0, 150, Sort.by("rating").descending()));
    }

    @Test
    void ??????_?????????_??????_?????????_??????() throws Exception {
        // given
        given(reviewService.findPageByProductId(anyLong(), any(Pageable.class)))
                .willReturn(ReviewPageResponse.from(
                        new SliceImpl<>(List.of(REVIEW_RATING_5.??????(1L, KEYBOARD_1.??????(), CORINNE.??????(1L))))));

        // when
        mockMvc.perform(get("/api/v1/keyboards/" + PRODUCT_ID + "/reviews?size=150&page=0&sort=rating,desc"))
                .andExpect(status().isOk())
                .andDo(print());

        // then
        verify(reviewService).findPageByProductId(PRODUCT_ID, PageRequest.of(0, 150, Sort.by("rating").descending()));
    }

    @Test
    void ??????_?????????_??????_?????????_??????_??????_??????_??????_????????????() throws Exception {
        // given
        given(reviewService.findPageByProductId(anyLong(), any(Pageable.class)))
                .willThrow(new KeyboardNotFoundException());

        // when
        mockMvc.perform(get("/api/v1/keyboards/" + 0L + "/reviews?size=150&page=0&sort=rating,desc"))
                .andExpect(status().isNotFound())
                .andDo(print());

        // then
        verify(reviewService).findPageByProductId(0L, PageRequest.of(0, 150, Sort.by("rating").descending()));
    }

    @Test
    void ??????_??????_??????() throws Exception {
        // given
        String authorizationHeader = "Bearer Token";
        Long reviewId = 1L;
        Long memberId = 1L;
        ReviewRequest updateRequest = new ReviewRequest("????????? ??????", 4);
        given(jwtProvider.validateToken(authorizationHeader))
                .willReturn(true);
        given(jwtProvider.getPayload(authorizationHeader))
                .willReturn(memberId.toString());
        willDoNothing().given(reviewService)
                .update(eq(reviewId), eq(memberId), any(ReviewRequest.class));

        // when
        mockMvc.perform(
                        put("/api/v1/reviews/" + reviewId)
                                .header(HttpHeaders.AUTHORIZATION, authorizationHeader)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(updateRequest))
                ).andExpect(status().isNoContent())
                .andDo(print());

        // then
        assertAll(
                () -> verify(jwtProvider).validateToken(authorizationHeader),
                () -> verify(jwtProvider).getPayload(authorizationHeader),
                () -> verify(reviewService).update(eq(reviewId), eq(memberId), any(ReviewRequest.class))
        );
    }

    @Test
    void ??????_??????_??????_??????_????????????_??????() throws Exception {
        // given
        String authorizationHeader = "Bearer Token";
        Long reviewId = 1L;
        Long memberId = 1L;
        ReviewRequest updateRequest = new ReviewRequest("????????? ??????", 4);
        given(jwtProvider.validateToken(authorizationHeader))
                .willReturn(true);
        given(jwtProvider.getPayload(authorizationHeader))
                .willReturn(memberId.toString());
        willThrow(new MemberNotFoundException()).given(reviewService)
                .update(eq(reviewId), eq(memberId), any(ReviewRequest.class));

        // when
        mockMvc.perform(
                        put("/api/v1/reviews/" + reviewId)
                                .header(HttpHeaders.AUTHORIZATION, authorizationHeader)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(updateRequest))
                ).andExpect(status().isNotFound())
                .andDo(print());

        // then
        assertAll(
                () -> verify(jwtProvider).validateToken(authorizationHeader),
                () -> verify(jwtProvider).getPayload(authorizationHeader),
                () -> verify(reviewService).update(eq(reviewId), eq(memberId), any(ReviewRequest.class))
        );
    }

    @Test
    void ??????_??????_??????_??????_????????????_??????() throws Exception {
        // given
        String authorizationHeader = "Bearer Token";
        Long reviewId = 1L;
        Long memberId = 1L;
        ReviewRequest updateRequest = new ReviewRequest("????????? ??????", 4);
        given(jwtProvider.validateToken(authorizationHeader))
                .willReturn(true);
        given(jwtProvider.getPayload(authorizationHeader))
                .willReturn(memberId.toString());
        willThrow(new ReviewNotFoundException()).given(reviewService)
                .update(eq(reviewId), eq(memberId), any(ReviewRequest.class));

        // when
        mockMvc.perform(
                        put("/api/v1/reviews/" + reviewId)
                                .header(HttpHeaders.AUTHORIZATION, authorizationHeader)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(updateRequest))
                ).andExpect(status().isNotFound())
                .andDo(print());

        // then
        assertAll(
                () -> verify(jwtProvider).validateToken(authorizationHeader),
                () -> verify(jwtProvider).getPayload(authorizationHeader),
                () -> verify(reviewService).update(eq(reviewId), eq(memberId), any(ReviewRequest.class))
        );
    }

    @Test
    void ??????_??????_??????_????????????_??????() throws Exception {
        // given
        String authorizationHeader = "Bearer Token";
        Long reviewId = 1L;
        Long memberId = 1L;
        ReviewRequest updateRequest = new ReviewRequest("????????? ??????", 4);
        given(jwtProvider.validateToken(authorizationHeader))
                .willReturn(true);
        given(jwtProvider.getPayload(authorizationHeader))
                .willReturn(memberId.toString());
        willThrow(new NotAuthorException()).given(reviewService)
                .update(eq(reviewId), eq(memberId), any(ReviewRequest.class));

        // when
        mockMvc.perform(
                        put("/api/v1/reviews/" + reviewId)
                                .header(HttpHeaders.AUTHORIZATION, authorizationHeader)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(updateRequest))
                ).andExpect(status().isForbidden())
                .andDo(print());

        // then
        assertAll(
                () -> verify(jwtProvider).validateToken(authorizationHeader),
                () -> verify(jwtProvider).getPayload(authorizationHeader),
                () -> verify(reviewService).update(eq(reviewId), eq(memberId), any(ReviewRequest.class))
        );
    }

    @Test
    void ??????_??????_??????() throws Exception {
        // given
        String authorizationHeader = "Bearer Token";
        Long reviewId = 1L;
        Long memberId = 1L;
        given(jwtProvider.validateToken(authorizationHeader))
                .willReturn(true);
        given(jwtProvider.getPayload(authorizationHeader))
                .willReturn(memberId.toString());
        willDoNothing().given(reviewService)
                .delete(reviewId, memberId);

        // when
        mockMvc.perform(
                        delete("/api/v1/reviews/" + reviewId)
                                .header(HttpHeaders.AUTHORIZATION, authorizationHeader)
                ).andExpect(status().isNoContent())
                .andDo(print());

        // then
        assertAll(
                () -> verify(jwtProvider).validateToken(authorizationHeader),
                () -> verify(jwtProvider).getPayload(authorizationHeader),
                () -> verify(reviewService).delete(reviewId, memberId)
        );
    }

    @Test
    void ??????_??????_??????_??????_????????????_??????() throws Exception {
        // given
        String authorizationHeader = "Bearer Token";
        Long reviewId = 1L;
        Long memberId = 1L;
        given(jwtProvider.validateToken(authorizationHeader))
                .willReturn(true);
        given(jwtProvider.getPayload(authorizationHeader))
                .willReturn(memberId.toString());
        willThrow(new MemberNotFoundException()).given(reviewService)
                .delete(reviewId, memberId);

        // when
        mockMvc.perform(
                        delete("/api/v1/reviews/" + reviewId)
                                .header(HttpHeaders.AUTHORIZATION, authorizationHeader)
                ).andExpect(status().isNotFound())
                .andDo(print());

        // then
        assertAll(
                () -> verify(jwtProvider).validateToken(authorizationHeader),
                () -> verify(jwtProvider).getPayload(authorizationHeader),
                () -> verify(reviewService).delete(reviewId, memberId)
        );
    }

    @Test
    void ??????_??????_??????_??????_????????????_??????() throws Exception {
        // given
        String authorizationHeader = "Bearer Token";
        Long reviewId = 1L;
        Long memberId = 1L;
        given(jwtProvider.validateToken(authorizationHeader))
                .willReturn(true);
        given(jwtProvider.getPayload(authorizationHeader))
                .willReturn(memberId.toString());
        willThrow(new ReviewNotFoundException()).given(reviewService)
                .delete(reviewId, memberId);

        // when
        mockMvc.perform(
                        delete("/api/v1/reviews/" + reviewId)
                                .header(HttpHeaders.AUTHORIZATION, authorizationHeader)
                ).andExpect(status().isNotFound())
                .andDo(print());

        // then
        assertAll(
                () -> verify(jwtProvider).validateToken(authorizationHeader),
                () -> verify(jwtProvider).getPayload(authorizationHeader),
                () -> verify(reviewService).delete(reviewId, memberId)
        );
    }

    @Test
    void ??????_??????_??????_????????????_??????() throws Exception {
        // given
        String authorizationHeader = "Bearer Token";
        Long reviewId = 1L;
        Long memberId = 1L;
        given(jwtProvider.validateToken(authorizationHeader))
                .willReturn(true);
        given(jwtProvider.getPayload(authorizationHeader))
                .willReturn(memberId.toString());
        willThrow(new NotAuthorException()).given(reviewService)
                .delete(reviewId, memberId);

        // when
        mockMvc.perform(
                        delete("/api/v1/reviews/" + reviewId)
                                .header(HttpHeaders.AUTHORIZATION, authorizationHeader)
                ).andExpect(status().isForbidden())
                .andDo(print());

        // then
        assertAll(
                () -> verify(jwtProvider).validateToken(authorizationHeader),
                () -> verify(jwtProvider).getPayload(authorizationHeader),
                () -> verify(reviewService).delete(reviewId, memberId)
        );
    }
}
