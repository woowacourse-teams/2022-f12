package com.woowacourse.f12.application;

import static com.woowacourse.f12.support.KeyboardFixtures.KEYBOARD_1;
import static com.woowacourse.f12.support.KeyboardFixtures.KEYBOARD_2;
import static com.woowacourse.f12.support.MemberFixtures.CORINNE;
import static com.woowacourse.f12.support.ReviewFixtures.REVIEW_RATING_5;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.BDDMockito.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.woowacourse.f12.domain.Keyboard;
import com.woowacourse.f12.domain.KeyboardRepository;
import com.woowacourse.f12.domain.MemberRepository;
import com.woowacourse.f12.domain.Review;
import com.woowacourse.f12.domain.ReviewRepository;
import com.woowacourse.f12.dto.request.ReviewRequest;
import com.woowacourse.f12.dto.response.ReviewPageResponse;
import com.woowacourse.f12.dto.response.ReviewWithProductPageResponse;
import com.woowacourse.f12.exception.KeyboardNotFoundException;
import java.util.List;
import java.util.Optional;
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

    @Mock
    private KeyboardRepository keyboardRepository;

    @Mock
    private MemberRepository memberRepository;

    @InjectMocks
    private ReviewService reviewService;

    @Test
    void 리뷰를_저장한다() {
        // given
        ReviewRequest reviewRequest = new ReviewRequest("내용", 5);
        Long productId = 1L;
        Keyboard keyboard = KEYBOARD_1.생성(productId);
        given(keyboardRepository.findById(productId))
                .willReturn(Optional.of(keyboard));
        given(memberRepository.findById(1L))
                .willReturn(Optional.of(CORINNE.생성(1L)));
        given(reviewRepository.save(reviewRequest.toReview(keyboard)))
                .willReturn(REVIEW_RATING_5.작성(1L, keyboard));

        // when
        Long reviewId = reviewService.save(productId, reviewRequest, 1L);

        // then
        assertAll(
                () -> assertThat(reviewId).isEqualTo(1L),
                () -> verify(keyboardRepository).findById(productId),
                () -> verify(reviewRepository).save(any(Review.class))
        );
    }

    @Test
    void 존재하지_않는_제품_리뷰_등록하면_예외가_발생한다() {
        // given
        ReviewRequest reviewRequest = new ReviewRequest("내용", 5);
        Long productId = 1L;

        given(keyboardRepository.findById(productId))
                .willReturn(Optional.empty());

        // when, then
        assertAll(
                () -> assertThatThrownBy(() -> reviewService.save(1L, reviewRequest, 1L))
                        .isExactlyInstanceOf(KeyboardNotFoundException.class),
                () -> verify(keyboardRepository).findById(productId),
                () -> verify(reviewRepository, times(0)).save(any(Review.class))
        );
    }

    @Test
    void 특정_제품에_대한_리뷰_목록을_조회한다() {
        // given
        Long productId = 1L;
        Keyboard keyboard = KEYBOARD_1.생성();
        Pageable pageable = PageRequest.of(0, 1, Sort.by(Order.desc("createdAt")));
        Slice<Review> slice = new SliceImpl<>(List.of(
                REVIEW_RATING_5.작성(1L, keyboard)
        ), pageable, true);

        given(keyboardRepository.existsById(productId))
                .willReturn(true);
        given(reviewRepository.findPageByProductId(productId, pageable))
                .willReturn(slice);

        // when
        ReviewPageResponse reviewPageResponse = reviewService.findPageByProductId(productId, pageable);

        // then
        assertAll(
                () -> assertThat(reviewPageResponse.getItems()).hasSize(1)
                        .extracting("id")
                        .containsExactly(1L),
                () -> assertThat(reviewPageResponse.isHasNext()).isTrue(),
                () -> verify(keyboardRepository).existsById(productId),
                () -> verify(reviewRepository).findPageByProductId(productId, pageable)
        );
    }

    @Test
    void 존재하지_않는_제품에_대한_리뷰_목록을_조회하면_예외가_발생한다() {
        // given
        Pageable pageable = PageRequest.of(0, 1, Sort.by(Order.desc("createdAt")));
        given(keyboardRepository.existsById(0L))
                .willReturn(false);

        // when, then
        assertAll(
                () -> assertThatThrownBy(() -> reviewService.findPageByProductId(0L, pageable))
                        .isExactlyInstanceOf(KeyboardNotFoundException.class),
                () -> verify(keyboardRepository).existsById(0L)
        );
    }

    @Test
    void 전체_리뷰_목록을_조회한다() {
        // given
        Pageable pageable = PageRequest.of(0, 2, Sort.by(Order.desc("createdAt")));
        Slice<Review> slice = new SliceImpl<>(List.of(
                REVIEW_RATING_5.작성(3L, KEYBOARD_1.생성()),
                REVIEW_RATING_5.작성(2L, KEYBOARD_2.생성())
        ), pageable, true);

        given(reviewRepository.findPageBy(pageable))
                .willReturn(slice);

        // when
        ReviewWithProductPageResponse reviewWithProductPageResponse = reviewService.findPage(pageable);

        // then
        assertAll(
                () -> assertThat(reviewWithProductPageResponse.getItems()).hasSize(2)
                        .extracting("id")
                        .containsExactly(3L, 2L),
                () -> assertThat(reviewWithProductPageResponse.isHasNext()).isTrue(),
                () -> verify(reviewRepository).findPageBy(pageable)
        );
    }
}
