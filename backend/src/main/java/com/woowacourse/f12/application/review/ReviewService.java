package com.woowacourse.f12.application.review;

import com.woowacourse.f12.domain.inventoryproduct.InventoryProduct;
import com.woowacourse.f12.domain.inventoryproduct.InventoryProductRepository;
import com.woowacourse.f12.domain.member.Member;
import com.woowacourse.f12.domain.member.MemberRepository;
import com.woowacourse.f12.domain.product.Product;
import com.woowacourse.f12.domain.product.ProductRepository;
import com.woowacourse.f12.domain.review.Review;
import com.woowacourse.f12.domain.review.ReviewRepository;
import com.woowacourse.f12.dto.request.review.ReviewRequest;
import com.woowacourse.f12.dto.response.review.ReviewPageResponse;
import com.woowacourse.f12.dto.response.review.ReviewWithProductPageResponse;
import com.woowacourse.f12.exception.badrequest.AlreadyWrittenReviewException;
import com.woowacourse.f12.exception.forbidden.NotAuthorException;
import com.woowacourse.f12.exception.notfound.KeyboardNotFoundException;
import com.woowacourse.f12.exception.notfound.MemberNotFoundException;
import com.woowacourse.f12.exception.notfound.ReviewNotFoundException;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final ProductRepository productRepository;
    private final MemberRepository memberRepository;
    private final InventoryProductRepository inventoryProductRepository;

    public ReviewService(final ReviewRepository reviewRepository, final ProductRepository productRepository,
                         final MemberRepository memberRepository,
                         final InventoryProductRepository inventoryProductRepository) {
        this.reviewRepository = reviewRepository;
        this.productRepository = productRepository;
        this.memberRepository = memberRepository;
        this.inventoryProductRepository = inventoryProductRepository;
    }

    @Transactional
    public Long saveReviewAndInventoryProduct(final Long productId, final Long memberId,
                                              final ReviewRequest reviewRequest) {
        final Member member = memberRepository.findById(memberId)
                .orElseThrow(MemberNotFoundException::new);
        final Product product = productRepository.findById(productId)
                .orElseThrow(KeyboardNotFoundException::new);
        final Long reviewId = saveReview(reviewRequest, member, product);
        saveInventoryProduct(memberId, product);
        return reviewId;
    }

    private Long saveReview(final ReviewRequest reviewRequest, final Member member, final Product product) {
        validateNotWritten(member, product);
        final Review review = reviewRequest.toReview(product, member);
        return reviewRepository.save(review)
                .getId();
    }

    private void validateNotWritten(final Member member, final Product product) {
        if (reviewRepository.existsByMemberAndProduct(member, product)) {
            throw new AlreadyWrittenReviewException();
        }
    }

    private void saveInventoryProduct(final Long memberId, final Product product) {
        if (inventoryProductRepository.existsByMemberIdAndProduct(memberId, product)) {
            return;
        }
        final InventoryProduct inventoryProduct = InventoryProduct.builder()
                .memberId(memberId)
                .product(product)
                .build();
        inventoryProductRepository.save(inventoryProduct);
    }

    public ReviewPageResponse findPageByProductId(final Long productId, final Pageable pageable) {
        validateKeyboardExists(productId);
        final Slice<Review> page = reviewRepository.findPageByProductId(productId, pageable);
        return ReviewPageResponse.from(page);
    }

    private void validateKeyboardExists(final Long productId) {
        if (!productRepository.existsById(productId)) {
            throw new KeyboardNotFoundException();
        }
    }

    public ReviewWithProductPageResponse findPage(final Pageable pageable) {
        final Slice<Review> page = reviewRepository.findPageBy(pageable);
        return ReviewWithProductPageResponse.from(page);
    }

    @Transactional
    public void update(final Long reviewId, final Long memberId, final ReviewRequest updateRequest) {
        final Review target = findTarget(reviewId, memberId);
        final Review updateReview = updateRequest.toReview(target.getProduct(), target.getMember());
        target.update(updateReview);
    }

    @Transactional
    public void delete(final Long reviewId, final Long memberId) {
        final Review target = findTarget(reviewId, memberId);
        reviewRepository.delete(target);
    }

    private Review findTarget(final Long reviewId, final Long memberId) {
        final Member member = memberRepository.findById(memberId)
                .orElseThrow(MemberNotFoundException::new);
        final Review target = reviewRepository.findById(reviewId)
                .orElseThrow(ReviewNotFoundException::new);
        validateAuthor(member, target);
        return target;
    }

    private void validateAuthor(final Member member, final Review target) {
        if (!target.isWrittenBy(member)) {
            throw new NotAuthorException();
        }
    }
}
