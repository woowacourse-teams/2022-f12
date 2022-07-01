package com.woowacourse.f12.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.BDDMockito.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import com.woowacourse.f12.domain.Review;
import com.woowacourse.f12.domain.ReviewRepository;
import com.woowacourse.f12.dto.request.ReviewRequest;
import com.woowacourse.f12.dto.response.ReviewPageResponse;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;

@ExtendWith(MockitoExtension.class)
class ReviewServiceTest {

    @Mock
    private ReviewRepository reviewRepository;

    @InjectMocks
    private ReviewService reviewService;

    @Test
    void 리뷰를_저장한다() {
        // given
        ReviewRequest reviewRequest = new ReviewRequest("내용", 5);
        Long productId = 1L;

        given(reviewRepository.save(reviewRequest.toReview(productId)))
                .willReturn(Review.builder()
                        .id(1L)
                        .build());

        // when
        Long reviewId = reviewService.save(productId, reviewRequest);

        // then
        assertAll(
                () -> assertThat(reviewId).isEqualTo(1L),
                () -> verify(reviewRepository).save(any(Review.class))
        );
    }

    @Test
    void 특정_제품에_대한_리뷰_목록을_조회한다() {
        // given
        Long productId = 1L;
        Pageable pageable = PageRequest.of(0, 1, Sort.by(Order.desc("createdAt")));
        Slice<Review> slice = new SliceImpl<>(List.of(
                리뷰_저장(2L, productId, "내용", 5)
        ), pageable, true);

        given(reviewRepository.findPageByProductId(productId, pageable))
                .willReturn(slice);

        // when
        ReviewPageResponse reviewPageResponse = reviewService.findPageByProductId(productId, pageable);

        // then
        assertAll(
                () -> assertThat(reviewPageResponse.getReviews()).hasSize(1)
                        .extracting("id")
                        .containsExactly(2L),
                () -> assertThat(reviewPageResponse.isHasNext()).isTrue(),
                () -> verify(reviewRepository).findPageByProductId(productId, pageable)
        );
    }

    private Review 리뷰_저장(Long id, Long productId, String content, int rating) {
        return Review.builder()
                .id(id)
                .productId(productId)
                .content(content)
                .rating(rating)
                .build();
    }
}
