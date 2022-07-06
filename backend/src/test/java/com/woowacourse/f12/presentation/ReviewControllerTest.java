package com.woowacourse.f12.presentation;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.woowacourse.f12.application.ReviewService;
import com.woowacourse.f12.domain.Review;
import com.woowacourse.f12.dto.request.ReviewRequest;
import com.woowacourse.f12.dto.response.ReviewPageResponse;
import com.woowacourse.f12.exception.BlankContentException;
import com.woowacourse.f12.exception.InvalidContentLengthException;
import com.woowacourse.f12.exception.InvalidRatingValueException;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.SliceImpl;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(ReviewController.class)
class ReviewControllerTest {

    private static final long PRODUCT_ID = 1L;
    private static final Review REVIEW_1 = Review.builder()
            .rating(5)
            .content("content")
            .productId(PRODUCT_ID)
            .createdAt(LocalDateTime.now())
            .build();

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper;

    @MockBean
    private ReviewService reviewService;

    public ReviewControllerTest() {
        this.objectMapper = new ObjectMapper();
    }

    @Test
    void 리뷰_생성_성공() throws Exception {
        // given
        given(reviewService.save(anyLong(), any(ReviewRequest.class)))
                .willReturn(1L);
        ReviewRequest reviewRequest = new ReviewRequest("content", 5);

        // when
        mockMvc.perform(
                        post("/api/v1/keyboards/" + PRODUCT_ID + "/reviews")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(reviewRequest))
                ).andExpect(status().isCreated())
                .andDo(print());

        // then
        verify(reviewService).save(eq(PRODUCT_ID), any(ReviewRequest.class));
    }

    @Test
    void 리뷰_생성_실패_리뷰_내용이_존재하지_않음() throws Exception {
        // given
        given(reviewService.save(anyLong(), any(ReviewRequest.class)))
                .willThrow(new BlankContentException());
        ReviewRequest reviewRequest = new ReviewRequest("", 5);

        // when
        mockMvc.perform(
                        post("/api/v1/keyboards/" + PRODUCT_ID + "/reviews")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(reviewRequest))
                ).andExpect(status().isBadRequest())
                .andDo(print());

        // then
        verify(reviewService).save(eq(PRODUCT_ID), any(ReviewRequest.class));
    }

    @Test
    void 리뷰_생성_실패_리뷰_내용_최대_길이_초과() throws Exception {
        // given
        given(reviewService.save(anyLong(), any(ReviewRequest.class)))
                .willThrow(new InvalidContentLengthException(1000));
        String content = "a".repeat(1001);
        ReviewRequest reviewRequest = new ReviewRequest(content, 5);

        // when
        mockMvc.perform(
                        post("/api/v1/keyboards/" + PRODUCT_ID + "/reviews")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(reviewRequest))
                ).andExpect(status().isBadRequest())
                .andDo(print());

        // then
        verify(reviewService).save(eq(PRODUCT_ID), any(ReviewRequest.class));

    }

    @Test
    void 리뷰_생성_실패_평점이_1부터_5사이_정수가_아님() throws Exception {
        // given
        given(reviewService.save(anyLong(), any(ReviewRequest.class)))
                .willThrow(new InvalidRatingValueException());
        ReviewRequest reviewRequest = new ReviewRequest("내용", 0);

        // when
        mockMvc.perform(
                        post("/api/v1/keyboards/" + PRODUCT_ID + "/reviews")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(reviewRequest))
                ).andExpect(status().isBadRequest())
                .andDo(print());

        // then
        verify(reviewService).save(eq(PRODUCT_ID), any(ReviewRequest.class));
    }

    @Test
    void 전체_리뷰_페이지_조회_성공() throws Exception {
        // given
        given(reviewService.findPage(any(Pageable.class)))
                .willReturn(ReviewPageResponse.from(new SliceImpl<>(List.of(REVIEW_1))));

        // when
        mockMvc.perform(get("/api/v1/reviews?size=150&page=0&sort=rating,desc"))
                .andExpect(status().isOk())
                .andDo(print());

        // then
        verify(reviewService).findPage(PageRequest.of(0, 150, Sort.by("rating").descending()));
    }

    @Test
    void 특정_상품의_리뷰_페이지_조회() throws Exception {
        // given
        given(reviewService.findPageByProductId(anyLong(), any(Pageable.class)))
                .willReturn(ReviewPageResponse.from(new SliceImpl<>(List.of(REVIEW_1))));

        // when
        mockMvc.perform(get("/api/v1/keyboards/" + PRODUCT_ID + "/reviews?size=150&page=0&sort=rating,desc"))
                .andExpect(status().isOk())
                .andDo(print());

        // then
        verify(reviewService).findPageByProductId(PRODUCT_ID, PageRequest.of(0, 150, Sort.by("rating").descending()));
    }
}
