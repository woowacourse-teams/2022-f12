package com.woowacourse.f12.application;

import static com.woowacourse.f12.support.KeyboardFixtures.KEYBOARD_1;
import static com.woowacourse.f12.support.KeyboardFixtures.KEYBOARD_2;
import static com.woowacourse.f12.support.MemberFixtures.CORINNE;
import static com.woowacourse.f12.support.ReviewFixtures.REVIEW_RATING_5;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.BDDMockito.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.woowacourse.f12.domain.InventoryProduct;
import com.woowacourse.f12.domain.InventoryProductRepository;
import com.woowacourse.f12.domain.Keyboard;
import com.woowacourse.f12.domain.KeyboardRepository;
import com.woowacourse.f12.domain.Member;
import com.woowacourse.f12.domain.MemberRepository;
import com.woowacourse.f12.domain.Review;
import com.woowacourse.f12.domain.ReviewRepository;
import com.woowacourse.f12.dto.request.ReviewRequest;
import com.woowacourse.f12.dto.response.ReviewPageResponse;
import com.woowacourse.f12.dto.response.ReviewResponse;
import com.woowacourse.f12.dto.response.ReviewWithProductPageResponse;
import com.woowacourse.f12.dto.response.ReviewWithProductResponse;
import com.woowacourse.f12.exception.KeyboardNotFoundException;
import com.woowacourse.f12.exception.MemberNotFoundException;
import com.woowacourse.f12.exception.NotAuthorException;
import com.woowacourse.f12.exception.ReviewNotFoundException;
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

    @Mock
    private InventoryProductRepository inventoryProductRepository;

    @InjectMocks
    private ReviewService reviewService;

    @Test
    void ?????????_????????????_???????????????_?????????_????????????() {
        // given
        ReviewRequest reviewRequest = new ReviewRequest("??????", 5);
        Long productId = 1L;
        Keyboard keyboard = KEYBOARD_1.??????(productId);
        Long memberId = 1L;
        Member member = CORINNE.??????(memberId);
        InventoryProduct inventoryProduct = ????????????_?????????_????????????(keyboard, memberId);
        given(memberRepository.findById(1L))
                .willReturn(Optional.of(member));
        given(keyboardRepository.findById(productId))
                .willReturn(Optional.of(keyboard));
        given(inventoryProductRepository.save(inventoryProduct))
                .willReturn(inventoryProduct);
        given(reviewRepository.save(reviewRequest.toReview(keyboard, member)))
                .willReturn(REVIEW_RATING_5.??????(1L, keyboard, member));

        // when
        Long reviewId = reviewService.saveReviewAndInventoryProduct(productId, memberId, reviewRequest);

        // then
        assertAll(
                () -> assertThat(reviewId).isEqualTo(1L),
                () -> verify(keyboardRepository).findById(productId),
                () -> verify(memberRepository).findById(memberId),
                () -> verify(reviewRepository).save(any(Review.class)),
                () -> verify(inventoryProductRepository).save(inventoryProduct)
        );
    }

    @Test
    void ????????????_??????_????????????_???????????????_?????????_????????????_?????????_????????????() {
        // given
        Long memberId = 1L;
        Long productId = 1L;
        ReviewRequest reviewRequest = new ReviewRequest("??????", 5);

        given(memberRepository.findById(memberId))
                .willReturn(Optional.empty());

        // when, then
        assertAll(
                () -> assertThatThrownBy(
                        () -> reviewService.saveReviewAndInventoryProduct(productId, memberId, reviewRequest))
                        .isExactlyInstanceOf(MemberNotFoundException.class),
                () -> verify(memberRepository).findById(memberId),
                () -> verify(reviewRepository, times(0)).save(any(Review.class))
        );
    }

    @Test
    void ????????????_??????_??????_??????_????????????_?????????_????????????() {
        // given
        Long memberId = 1L;
        Long productId = 1L;
        Member member = CORINNE.??????(memberId);
        ReviewRequest reviewRequest = new ReviewRequest("??????", 5);

        given(memberRepository.findById(memberId))
                .willReturn(Optional.of(member));
        given(keyboardRepository.findById(productId))
                .willReturn(Optional.empty());

        // when, then
        assertAll(
                () -> assertThatThrownBy(() -> reviewService.saveReviewAndInventoryProduct(1L, 1L, reviewRequest))
                        .isExactlyInstanceOf(KeyboardNotFoundException.class),
                () -> verify(memberRepository).findById(memberId),
                () -> verify(keyboardRepository).findById(productId),
                () -> verify(reviewRepository, times(0)).save(any(Review.class))
        );
    }

    @Test
    void ??????_?????????_??????_??????_?????????_????????????() {
        // given
        Long productId = 1L;
        Keyboard keyboard = KEYBOARD_1.??????();
        Member member = CORINNE.??????(1L);
        Pageable pageable = PageRequest.of(0, 1, Sort.by(Order.desc("createdAt")));
        Review review = REVIEW_RATING_5.??????(1L, keyboard, member);
        Slice<Review> slice = new SliceImpl<>(List.of(review), pageable, true);

        given(keyboardRepository.existsById(productId))
                .willReturn(true);
        given(reviewRepository.findPageByProductId(productId, pageable))
                .willReturn(slice);

        // when
        ReviewPageResponse reviewPageResponse = reviewService.findPageByProductId(productId, pageable);

        // then
        assertAll(
                () -> assertThat(reviewPageResponse.getItems()).hasSize(1)
                        .usingRecursiveFieldByFieldElementComparator()
                        .containsOnly(ReviewResponse.from(review)),
                () -> assertThat(reviewPageResponse.isHasNext()).isTrue(),
                () -> verify(keyboardRepository).existsById(productId),
                () -> verify(reviewRepository).findPageByProductId(productId, pageable)
        );
    }

    @Test
    void ????????????_??????_?????????_??????_??????_?????????_????????????_?????????_????????????() {
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
    void ??????_??????_?????????_????????????() {
        // given
        Pageable pageable = PageRequest.of(0, 2, Sort.by(Order.desc("createdAt")));
        Member member = CORINNE.??????(1L);
        Review review1 = REVIEW_RATING_5.??????(3L, KEYBOARD_1.??????(), member);
        Review review2 = REVIEW_RATING_5.??????(2L, KEYBOARD_2.??????(), member);
        Slice<Review> slice = new SliceImpl<>(List.of(review1, review2), pageable, true);

        given(reviewRepository.findPageBy(pageable))
                .willReturn(slice);

        // when
        ReviewWithProductPageResponse reviewWithProductPageResponse = reviewService.findPage(pageable);

        // then
        assertAll(
                () -> assertThat(reviewWithProductPageResponse.getItems()).hasSize(2)
                        .usingRecursiveFieldByFieldElementComparator()
                        .containsOnly(ReviewWithProductResponse.from(review1), ReviewWithProductResponse.from(review2)),
                () -> assertThat(reviewWithProductPageResponse.isHasNext()).isTrue(),
                () -> verify(reviewRepository).findPageBy(pageable)
        );
    }

    @Test
    void ????????????_?????????_??????_????????????_????????????_?????????_????????????() {
        // given
        Long reviewId = 1L;
        Long memberId = 1L;
        Member member = CORINNE.??????(memberId);
        Review review = REVIEW_RATING_5.??????(reviewId, KEYBOARD_1.??????(), member);
        ReviewRequest updateRequest = new ReviewRequest("????????? ??????", 4);

        given(memberRepository.findById(memberId))
                .willReturn(Optional.of(member));
        given(reviewRepository.findById(reviewId))
                .willReturn(Optional.of(review));

        // when
        reviewService.update(reviewId, memberId, updateRequest);

        // then
        assertAll(
                () -> assertThat(review).usingRecursiveComparison()
                        .comparingOnlyFields("content", "rating")
                        .isEqualTo(updateRequest.toReview(null, null)),
                () -> verify(memberRepository).findById(memberId),
                () -> verify(reviewRepository).findById(reviewId)
        );
    }

    @Test
    void ????????????_??????_????????????_???????????????_?????????_????????????_??????_?????????_????????????() {
        // given
        Long reviewId = 1L;
        Long memberId = 1L;
        ReviewRequest updateRequest = new ReviewRequest("????????? ??????", 4);

        given(memberRepository.findById(memberId))
                .willReturn(Optional.empty());

        // when, then
        assertAll(
                () -> assertThatThrownBy(() -> reviewService.update(reviewId, memberId, updateRequest))
                        .isExactlyInstanceOf(MemberNotFoundException.class),
                () -> verify(memberRepository).findById(memberId),
                () -> verify(reviewRepository, times(0)).findById(reviewId)
        );
    }

    @Test
    void ???????????????_?????????_?????????_?????????_????????????() {
        // given
        Long reviewId = 1L;
        Long memberId = 1L;
        ReviewRequest updateRequest = new ReviewRequest("????????? ??????", 4);
        Member member = CORINNE.??????(memberId);

        given(memberRepository.findById(memberId))
                .willReturn(Optional.of(member));
        given(reviewRepository.findById(reviewId))
                .willReturn(Optional.empty());

        // when, then
        assertAll(
                () -> assertThatThrownBy(() -> reviewService.update(reviewId, memberId, updateRequest))
                        .isExactlyInstanceOf(ReviewNotFoundException.class),
                () -> verify(memberRepository).findById(memberId),
                () -> verify(reviewRepository).findById(reviewId)
        );
    }

    @Test
    void ??????_?????????_????????????_?????????_??????_????????????_?????????_?????????_????????????() {
        // given
        Long reviewId = 1L;
        Long memberId = 1L;
        Long notAuthorId = 0L;
        Member member = CORINNE.??????(memberId);
        Member notAuthor = CORINNE.??????(notAuthorId);
        Review review = REVIEW_RATING_5.??????(reviewId, KEYBOARD_1.??????(), member);
        ReviewRequest updateRequest = new ReviewRequest("????????? ??????", 4);

        given(memberRepository.findById(notAuthorId))
                .willReturn(Optional.of(notAuthor));
        given(reviewRepository.findById(reviewId))
                .willReturn(Optional.of(review));

        // when, then
        assertAll(
                () -> assertThatThrownBy(() -> reviewService.update(reviewId, notAuthorId, updateRequest))
                        .isExactlyInstanceOf(NotAuthorException.class),
                () -> verify(memberRepository).findById(notAuthorId),
                () -> verify(reviewRepository).findById(reviewId)
        );
    }

    @Test
    void ????????????_?????????_??????_????????????_????????????_????????????() {
        // given
        Long reviewId = 1L;
        Long memberId = 1L;
        Member member = CORINNE.??????(memberId);
        Review review = REVIEW_RATING_5.??????(reviewId, KEYBOARD_1.??????(), member);

        given(memberRepository.findById(memberId))
                .willReturn(Optional.of(member));
        given(reviewRepository.findById(reviewId))
                .willReturn(Optional.of(review));
        willDoNothing().given(reviewRepository)
                .delete(any(Review.class));

        // when, then
        assertAll(
                () -> assertDoesNotThrow(() -> reviewService.delete(reviewId, memberId)),
                () -> verify(memberRepository).findById(memberId),
                () -> verify(reviewRepository).findById(reviewId),
                () -> verify(reviewRepository).delete(review)
        );
    }

    @Test
    void ????????????_??????_????????????_???????????????_?????????_????????????_??????_?????????_????????????() {
        // given
        Long memberId = 1L;
        Long reviewId = 1L;
        given(memberRepository.findById(memberId))
                .willReturn(Optional.empty());

        // when, then
        assertAll(
                () -> assertThatThrownBy(() -> reviewService.delete(reviewId, memberId))
                        .isExactlyInstanceOf(MemberNotFoundException.class),
                () -> verify(memberRepository).findById(1L),
                () -> verify(reviewRepository, times(0)).findById(reviewId),
                () -> verify(reviewRepository, times(0)).delete(any(Review.class))
        );
    }

    @Test
    void ???????????????_?????????_?????????_?????????_????????????() {
        // given
        Long memberId = 1L;
        Long reviewId = 1L;
        Member member = CORINNE.??????(memberId);
        given(memberRepository.findById(memberId))
                .willReturn(Optional.of(member));
        given(reviewRepository.findById(reviewId))
                .willReturn(Optional.empty());

        // when, then
        assertAll(
                () -> assertThatThrownBy(() -> reviewService.delete(reviewId, memberId))
                        .isExactlyInstanceOf(ReviewNotFoundException.class),
                () -> verify(memberRepository).findById(memberId),
                () -> verify(reviewRepository).findById(reviewId),
                () -> verify(reviewRepository, times(0)).delete(any(Review.class))
        );
    }

    @Test
    void ??????_?????????_????????????_?????????_??????_????????????_?????????_?????????_????????????() {
        // given
        Long reviewId = 1L;
        Long memberId = 1L;
        Long notAuthorId = 0L;
        Member member = CORINNE.??????(memberId);
        Member notAuthor = CORINNE.??????(notAuthorId);
        Review review = REVIEW_RATING_5.??????(reviewId, KEYBOARD_1.??????(), member);

        given(memberRepository.findById(notAuthorId))
                .willReturn(Optional.of(notAuthor));
        given(reviewRepository.findById(reviewId))
                .willReturn(Optional.of(review));

        // when, then
        assertAll(
                () -> assertThatThrownBy(() -> reviewService.delete(reviewId, notAuthorId))
                        .isExactlyInstanceOf(NotAuthorException.class),
                () -> verify(memberRepository).findById(notAuthorId),
                () -> verify(reviewRepository).findById(reviewId),
                () -> verify(reviewRepository, times(0)).delete(review)
        );
    }

    private InventoryProduct ????????????_?????????_????????????(final Keyboard keyboard, final Long memberId) {
        return InventoryProduct.builder()
                .keyboard(keyboard)
                .memberId(memberId)
                .build();
    }
}
