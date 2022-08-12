package com.woowacourse.f12.application.review;

import static com.woowacourse.f12.support.InventoryProductFixtures.SELECTED_INVENTORY_PRODUCT;
import static com.woowacourse.f12.support.InventoryProductFixtures.UNSELECTED_INVENTORY_PRODUCT;
import static com.woowacourse.f12.support.MemberFixtures.CORINNE;
import static com.woowacourse.f12.support.MemberFixtures.NOT_ADDITIONAL_INFO;
import static com.woowacourse.f12.support.ProductFixture.KEYBOARD_1;
import static com.woowacourse.f12.support.ProductFixture.KEYBOARD_2;
import static com.woowacourse.f12.support.ReviewFixtures.REVIEW_RATING_1;
import static com.woowacourse.f12.support.ReviewFixtures.REVIEW_RATING_5;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.refEq;
import static org.mockito.BDDMockito.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.woowacourse.f12.domain.inventoryproduct.InventoryProduct;
import com.woowacourse.f12.domain.inventoryproduct.InventoryProductRepository;
import com.woowacourse.f12.domain.member.Member;
import com.woowacourse.f12.domain.member.MemberRepository;
import com.woowacourse.f12.domain.product.Product;
import com.woowacourse.f12.domain.product.ProductRepository;
import com.woowacourse.f12.domain.review.Review;
import com.woowacourse.f12.domain.review.ReviewRepository;
import com.woowacourse.f12.dto.request.review.ReviewRequest;
import com.woowacourse.f12.dto.response.review.ReviewWithAuthorAndProductPageResponse;
import com.woowacourse.f12.dto.response.review.ReviewWithAuthorAndProductResponse;
import com.woowacourse.f12.dto.response.review.ReviewWithAuthorPageResponse;
import com.woowacourse.f12.dto.response.review.ReviewWithAuthorResponse;
import com.woowacourse.f12.dto.response.review.ReviewWithProductPageResponse;
import com.woowacourse.f12.dto.response.review.ReviewWithProductResponse;
import com.woowacourse.f12.exception.badrequest.AlreadyWrittenReviewException;
import com.woowacourse.f12.exception.badrequest.InvalidProfileArgumentException;
import com.woowacourse.f12.exception.forbidden.NotAuthorException;
import com.woowacourse.f12.exception.notfound.InventoryProductNotFoundException;
import com.woowacourse.f12.exception.notfound.MemberNotFoundException;
import com.woowacourse.f12.exception.notfound.ProductNotFoundException;
import com.woowacourse.f12.exception.notfound.ReviewNotFoundException;
import com.woowacourse.f12.support.ReviewFixtures;
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
    private ProductRepository productRepository;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private InventoryProductRepository inventoryProductRepository;

    @InjectMocks
    private ReviewService reviewService;

    @Test
    void 리뷰를_저장하고_인벤토리에_제품을_추가한다() {
        // given
        ReviewRequest reviewRequest = new ReviewRequest("내용", 5);
        Long productId = 1L;
        Product product = KEYBOARD_1.생성(productId);
        Long memberId = 1L;
        Member member = CORINNE.생성(memberId);
        InventoryProduct inventoryProduct = UNSELECTED_INVENTORY_PRODUCT.생성(member, product);
        given(memberRepository.findById(1L))
                .willReturn(Optional.of(member));
        given(productRepository.findById(productId))
                .willReturn(Optional.of(product));
        given(inventoryProductRepository.existsByMemberAndProduct(member, product))
                .willReturn(false);
        given(inventoryProductRepository.save(inventoryProduct))
                .willReturn(inventoryProduct);
        given(reviewRepository.save(reviewRequest.toReview(product, member)))
                .willReturn(REVIEW_RATING_5.작성(1L, product, member));

        // when
        Long reviewId = reviewService.saveReviewAndInventoryProduct(productId, memberId, reviewRequest);

        // then
        assertAll(
                () -> assertThat(reviewId).isEqualTo(1L),
                () -> verify(productRepository).findById(productId),
                () -> verify(memberRepository).findById(memberId),
                () -> verify(reviewRepository).save(any(Review.class)),
                () -> verify(inventoryProductRepository).existsByMemberAndProduct(member, product),
                () -> verify(inventoryProductRepository).save(inventoryProduct)
        );
    }

    @Test
    void 존재하지_않는_회원으로_로그인하여_리뷰를_작성하면_예외를_반환한다() {
        // given
        Long memberId = 1L;
        Long productId = 1L;
        ReviewRequest reviewRequest = new ReviewRequest("내용", 5);

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
    void 추가정보가_없는_회원으로_로그인하여_리뷰를_작성하면_예외를_반환한다() {
        // given
        Long memberId = 1L;
        Long productId = 1L;
        ReviewRequest reviewRequest = new ReviewRequest("내용", 5);

        given(memberRepository.findById(memberId))
                .willReturn(Optional.of(NOT_ADDITIONAL_INFO.생성(1L)));

        // when, then
        assertAll(
                () -> assertThatThrownBy(
                        () -> reviewService.saveReviewAndInventoryProduct(productId, memberId, reviewRequest))
                        .isExactlyInstanceOf(InvalidProfileArgumentException.class),
                () -> verify(memberRepository).findById(memberId),
                () -> verify(reviewRepository, times(0)).save(any(Review.class))
        );
    }

    @Test
    void 존재하지_않는_제품_리뷰_등록하면_예외가_발생한다() {
        // given
        Long memberId = 1L;
        Long productId = 1L;
        Member member = CORINNE.생성(memberId);
        ReviewRequest reviewRequest = new ReviewRequest("내용", 5);

        given(memberRepository.findById(memberId))
                .willReturn(Optional.of(member));
        given(productRepository.findById(productId))
                .willReturn(Optional.empty());

        // when, then
        assertAll(
                () -> assertThatThrownBy(() -> reviewService.saveReviewAndInventoryProduct(1L, 1L, reviewRequest))
                        .isExactlyInstanceOf(ProductNotFoundException.class),
                () -> verify(memberRepository).findById(memberId),
                () -> verify(productRepository).findById(productId),
                () -> verify(reviewRepository, times(0)).save(any(Review.class))
        );
    }

    @Test
    void 이미_작성되어있는_제품의_리뷰를_등록하면_예외가_발생한다() {
        // given
        Long memberId = 1L;
        Long productId = 1L;
        Member member = CORINNE.생성(memberId);
        ReviewRequest reviewRequest = new ReviewRequest("내용", 5);
        Product product = KEYBOARD_1.생성(productId);

        given(memberRepository.findById(memberId))
                .willReturn(Optional.of(member));
        given(productRepository.findById(productId))
                .willReturn(Optional.of(product));
        given(reviewRepository.existsByMemberAndProduct(member, product))
                .willReturn(true);

        // when, then
        assertAll(
                () -> assertThatThrownBy(() -> reviewService.saveReviewAndInventoryProduct(1L, 1L, reviewRequest))
                        .isExactlyInstanceOf(AlreadyWrittenReviewException.class),
                () -> verify(memberRepository).findById(memberId),
                () -> verify(productRepository).findById(productId),
                () -> verify(reviewRepository).existsByMemberAndProduct(member, product),
                () -> verify(reviewRepository, times(0)).save(any(Review.class))
        );
    }

    @Test
    void 특정_제품에_대한_리뷰_목록을_조회한다() {
        // given
        Long productId = 1L;
        Product product = KEYBOARD_1.생성();
        Member member = CORINNE.생성(1L);
        Pageable pageable = PageRequest.of(0, 1, Sort.by(Order.desc("createdAt")));
        Review review = REVIEW_RATING_5.작성(1L, product, member);
        Slice<Review> slice = new SliceImpl<>(List.of(review), pageable, true);

        given(productRepository.existsById(productId))
                .willReturn(true);
        given(reviewRepository.findPageByProductId(productId, pageable))
                .willReturn(slice);

        // when
        ReviewWithAuthorPageResponse reviewPageResponse = reviewService.findPageByProductId(productId, pageable);

        // then
        assertAll(
                () -> assertThat(reviewPageResponse.getItems()).hasSize(1)
                        .usingRecursiveFieldByFieldElementComparator()
                        .containsOnly(ReviewWithAuthorResponse.from(review)),
                () -> assertThat(reviewPageResponse.isHasNext()).isTrue(),
                () -> verify(productRepository).existsById(productId),
                () -> verify(reviewRepository).findPageByProductId(productId, pageable)
        );
    }

    @Test
    void 존재하지_않는_제품에_대한_리뷰_목록을_조회하면_예외가_발생한다() {
        // given
        Pageable pageable = PageRequest.of(0, 1, Sort.by(Order.desc("createdAt")));
        given(productRepository.existsById(0L))
                .willReturn(false);

        // when, then
        assertAll(
                () -> assertThatThrownBy(() -> reviewService.findPageByProductId(0L, pageable))
                        .isExactlyInstanceOf(ProductNotFoundException.class),
                () -> verify(productRepository).existsById(0L)
        );
    }

    @Test
    void 전체_리뷰_목록을_조회한다() {
        // given
        Pageable pageable = PageRequest.of(0, 2, Sort.by(Order.desc("createdAt")));
        Member member = CORINNE.생성(1L);
        Review review1 = REVIEW_RATING_5.작성(3L, KEYBOARD_1.생성(), member);
        Review review2 = REVIEW_RATING_5.작성(2L, KEYBOARD_2.생성(), member);
        Slice<Review> slice = new SliceImpl<>(List.of(review1, review2), pageable, true);

        given(reviewRepository.findPageBy(pageable))
                .willReturn(slice);

        // when
        ReviewWithAuthorAndProductPageResponse reviewWithAuthorAndProductPageResponse = reviewService.findPage(
                pageable);

        // then
        assertAll(
                () -> assertThat(reviewWithAuthorAndProductPageResponse.getItems()).hasSize(2)
                        .usingRecursiveFieldByFieldElementComparator()
                        .containsOnly(ReviewWithAuthorAndProductResponse.from(review1),
                                ReviewWithAuthorAndProductResponse.from(review2)),
                () -> assertThat(reviewWithAuthorAndProductPageResponse.isHasNext()).isTrue(),
                () -> verify(reviewRepository).findPageBy(pageable)
        );
    }

    @Test
    void 로그인한_회원이_리뷰_작성자와_일치하면_리뷰를_수정한다() {
        // given
        Long reviewId = 1L;
        Long memberId = 1L;
        Member member = CORINNE.생성(memberId);
        Review review = REVIEW_RATING_5.작성(reviewId, KEYBOARD_1.생성(), member);
        ReviewRequest updateRequest = new ReviewRequest("수정할 내용", 4);

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
    void 존재하지_않는_회원으로_로그인하여_리뷰를_수정하려_하면_예외를_반환한다() {
        // given
        Long reviewId = 1L;
        Long memberId = 1L;
        ReviewRequest updateRequest = new ReviewRequest("수정할 내용", 4);

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
    void 수정하려는_리뷰가_없으면_예외를_반환한다() {
        // given
        Long reviewId = 1L;
        Long memberId = 1L;
        ReviewRequest updateRequest = new ReviewRequest("수정할 내용", 4);
        Member member = CORINNE.생성(memberId);

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
    void 수정_요청의_로그인한_회원이_리뷰_작성자가_아니면_예외를_반환한다() {
        // given
        Long reviewId = 1L;
        Long memberId = 1L;
        Long notAuthorId = 0L;
        Member member = CORINNE.생성(memberId);
        Member notAuthor = CORINNE.생성(notAuthorId);
        Review review = REVIEW_RATING_5.작성(reviewId, KEYBOARD_1.생성(), member);
        ReviewRequest updateRequest = new ReviewRequest("수정할 내용", 4);

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
    void 로그인한_회원이_리뷰_작성자와_일치하면_삭제한다() {
        // given
        Long reviewId = 1L;
        Long memberId = 1L;
        Member member = CORINNE.생성(memberId);
        Review review = REVIEW_RATING_5.작성(reviewId, KEYBOARD_1.생성(), member);

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
    void 존재하지_않는_회원으로_로그인하여_리뷰를_삭제하려_하면_예외를_반환한다() {
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
    void 삭제하려는_리뷰가_없으면_예외를_반환한다() {
        // given
        Long memberId = 1L;
        Long reviewId = 1L;
        Member member = CORINNE.생성(memberId);
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
    void 삭제_요청의_로그인한_회원이_리뷰_작성자가_아니면_예외를_반환한다() {
        // given
        Long reviewId = 1L;
        Long memberId = 1L;
        Long notAuthorId = 0L;
        Member member = CORINNE.생성(memberId);
        Member notAuthor = CORINNE.생성(notAuthorId);
        Review review = REVIEW_RATING_5.작성(reviewId, KEYBOARD_1.생성(), member);

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

    @Test
    void 회원_아이디로_회원이_작성한_리뷰_목록을_조회한다() {
        // given
        Member corinne = CORINNE.생성(1L);
        Review review1 = REVIEW_RATING_1.작성(1L, KEYBOARD_1.생성(1L), corinne);
        Review review2 = REVIEW_RATING_1.작성(1L, KEYBOARD_2.생성(1L), corinne);

        Pageable pageable = PageRequest.of(0, 1, Sort.by("createdAt").descending());
        given(memberRepository.existsById(anyLong()))
                .willReturn(true);
        given(reviewRepository.findPageByMemberId(anyLong(), any(PageRequest.class)))
                .willReturn(new SliceImpl<>(List.of(review2, review1), pageable, false));

        // when
        ReviewWithProductPageResponse reviewWithProductPageResponse = reviewService.findPageByMemberId(1L, pageable);

        // then
        assertAll(
                () -> assertThat(reviewWithProductPageResponse.getItems()).usingRecursiveFieldByFieldElementComparator()
                        .containsExactly(ReviewWithProductResponse.from(review2),
                                ReviewWithProductResponse.from(review1)),
                () -> verify(memberRepository).existsById(corinne.getId()),
                () -> verify(reviewRepository).findPageByMemberId(eq(corinne.getId()), eq(pageable))
        );
    }

    @Test
    void 회원이_작성한_리뷰_목록을_조회할때_회원이_존재하지_않으면_예외를_반환한다() {
        // given
        Pageable pageable = PageRequest.of(0, 1, Sort.by("createdAt").descending());
        given(memberRepository.existsById(anyLong()))
                .willReturn(false);

        // when, then
        assertAll(
                () -> assertThatThrownBy(
                        () -> reviewService.findPageByMemberId(1L, pageable)).isInstanceOf(
                        MemberNotFoundException.class),
                () -> verify(memberRepository).existsById(anyLong()),
                () -> verify(reviewRepository, times(0)).findPageByMemberId(anyLong(),
                        any(PageRequest.class))
        );
    }

    @Test
    void 인벤토리_아이디로_리뷰를_조회한다() {
        // given
        Review review = ReviewFixtures.REVIEW_RATING_1.작성(1L, KEYBOARD_1.생성(1L), CORINNE.생성(1L));
        given(inventoryProductRepository.findById(any(Long.class)))
                .willReturn(Optional.of(SELECTED_INVENTORY_PRODUCT.생성(CORINNE.생성(1L), KEYBOARD_1.생성(1L))));
        given(reviewRepository.findByMemberAndProduct(any(Member.class), any(Product.class)))
                .willReturn(Optional.of(review));

        // when
        ReviewWithProductResponse reviewResponse = reviewService.findByInventoryProductId(1L);

        // then
        assertAll(
                () -> assertThat(reviewResponse).usingRecursiveComparison()
                        .isEqualTo(ReviewWithProductResponse.from(review)),
                () -> verify(inventoryProductRepository).findById(1L),
                () -> verify(reviewRepository).findByMemberAndProduct(refEq(CORINNE.생성(1L)), refEq(KEYBOARD_1.생성(1L)))
        );
    }

    @Test
    void 인벤토리_아이디로_리뷰를_조회할때_등록된_장비가_존재하지_않는다면_예외가_발생한다() {
        // given
        given(inventoryProductRepository.findById(any(Long.class)))
                .willReturn(Optional.empty());

        // when, then
        assertAll(
                () -> assertThatThrownBy(() -> reviewService.findByInventoryProductId(1L))
                        .isInstanceOf(InventoryProductNotFoundException.class),
                () -> verify(inventoryProductRepository).findById(1L),
                () -> verify(reviewRepository, times(0)).findByMemberAndProduct(any(Member.class), any(Product.class))
        );
    }

    @Test
    void 인벤토리_아이디로_리뷰를_조회할때_리뷰가_존재하지_않는다면_예외가_발생한다() {
        //given
        Review review = ReviewFixtures.REVIEW_RATING_1.작성(1L, KEYBOARD_1.생성(1L), CORINNE.생성(1L));
        given(inventoryProductRepository.findById(any(Long.class)))
                .willReturn(Optional.of(SELECTED_INVENTORY_PRODUCT.생성(CORINNE.생성(1L), KEYBOARD_1.생성(1L))));
        given(reviewRepository.findByMemberAndProduct(any(Member.class), any(Product.class)))
                .willReturn(Optional.empty());

        // when, then
        assertAll(
                () -> assertThatThrownBy(() -> reviewService.findByInventoryProductId(1L))
                        .isInstanceOf(ReviewNotFoundException.class),
                () -> verify(inventoryProductRepository).findById(1L),
                () -> verify(reviewRepository).findByMemberAndProduct(refEq(CORINNE.생성(1L)), refEq(KEYBOARD_1.생성(1L)))
        );
    }
}
