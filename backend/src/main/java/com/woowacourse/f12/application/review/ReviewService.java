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
import com.woowacourse.f12.dto.response.review.ReviewWithAuthorAndProductPageResponse;
import com.woowacourse.f12.dto.response.review.ReviewWithAuthorPageResponse;
import com.woowacourse.f12.dto.response.review.ReviewWithProductPageResponse;
import com.woowacourse.f12.dto.response.review.ReviewWithProductResponse;
import com.woowacourse.f12.exception.badrequest.AlreadyWrittenReviewException;
import com.woowacourse.f12.exception.badrequest.RegisterNotCompletedException;
import com.woowacourse.f12.exception.forbidden.NotAuthorException;
import com.woowacourse.f12.exception.notfound.InventoryProductNotFoundException;
import com.woowacourse.f12.exception.notfound.MemberNotFoundException;
import com.woowacourse.f12.exception.notfound.ProductNotFoundException;
import com.woowacourse.f12.exception.notfound.ReviewNotFoundException;
import java.util.Objects;
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
        final Member member = findMemberById(memberId);
        validateRegisterCompleted(member);
        final Product product = productRepository.findById(productId)
                .orElseThrow(ProductNotFoundException::new);
        final Long reviewId = saveReview(reviewRequest, member, product);
        saveInventoryProduct(member, product);
        return reviewId;
    }

    private void validateRegisterCompleted(final Member member) {
        if (!member.isRegisterCompleted()) {
            throw new RegisterNotCompletedException();
        }
    }

    private Long saveReview(final ReviewRequest reviewRequest, final Member member, final Product product) {
        validateNotWritten(member, product);
        final Review review = reviewRequest.toReview(product, member);
        product.reflectReview(review);
        return reviewRepository.save(review)
                .getId();
    }

    private void validateNotWritten(final Member member, final Product product) {
        if (reviewRepository.existsByMemberAndProduct(member, product)) {
            throw new AlreadyWrittenReviewException();
        }
    }

    private void saveInventoryProduct(final Member member, final Product product) {
        if (inventoryProductRepository.existsByMemberAndProduct(member, product)) {
            return;
        }
        final InventoryProduct inventoryProduct = InventoryProduct.builder()
                .member(member)
                .product(product)
                .build();
        inventoryProductRepository.save(inventoryProduct);
    }

    public ReviewWithAuthorPageResponse findPageByProductId(final Long productId, final Long memberId,
                                                            final Pageable pageable) {
        validateKeyboardExists(productId);
        final Slice<Review> page = reviewRepository.findPageByProductId(productId, pageable);
        if (Objects.isNull(memberId)) {
            return ReviewWithAuthorPageResponse.from(page);
        }
        return ReviewWithAuthorPageResponse.of(page, memberId);
    }

    private void validateKeyboardExists(final Long productId) {
        if (!productRepository.existsById(productId)) {
            throw new ProductNotFoundException();
        }
    }

    public ReviewWithAuthorAndProductPageResponse findPage(final Pageable pageable) {
        final Slice<Review> page = reviewRepository.findPageBy(pageable);
        return ReviewWithAuthorAndProductPageResponse.from(page);
    }

    @Transactional
    public void update(final Long reviewId, final Long memberId, final ReviewRequest updateRequest) {
        final Review target = findTarget(reviewId, memberId);
        final Review updateReview = updateRequest.toReview(target.getProduct(), target.getMember());
        target.update(updateReview);
    }

    @Transactional
    public void delete(final Long reviewId, final Long memberId) {
        final Review review = findTarget(reviewId, memberId);
        reviewRepository.delete(review);
        final InventoryProduct inventoryProduct = inventoryProductRepository.findWithProductByMemberAndProduct(
                        review.getMember(), review.getProduct())
                .orElseThrow(InventoryProductNotFoundException::new);
        inventoryProductRepository.delete(inventoryProduct);
    }

    private Review findTarget(final Long reviewId, final Long memberId) {
        final Member member = findMemberById(memberId);
        final Review target = findReviewById(reviewId);
        validateAuthor(member, target);
        return target;
    }

    private Member findMemberById(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(MemberNotFoundException::new);
    }

    private Review findReviewById(Long reviewId) {
        return reviewRepository.findById(reviewId)
                .orElseThrow(ReviewNotFoundException::new);
    }

    private void validateAuthor(final Member member, final Review target) {
        if (!target.isWrittenBy(member)) {
            throw new NotAuthorException();
        }
    }

    public ReviewWithProductPageResponse findPageByMemberId(final Long memberId, final Pageable pageable) {
        validateMemberExist(memberId);
        final Slice<Review> page = reviewRepository.findPageByMemberId(memberId, pageable);

        return ReviewWithProductPageResponse.from(page);
    }

    private void validateMemberExist(final Long memberId) {
        if (!memberRepository.existsById(memberId)) {
            throw new MemberNotFoundException();
        }
    }

    public ReviewWithProductResponse findByInventoryProductId(final Long inventoryProductId) {
        final InventoryProduct inventoryProduct = inventoryProductRepository.findById(inventoryProductId)
                .orElseThrow(InventoryProductNotFoundException::new);

        final Review review = reviewRepository.findByMemberAndProduct(
                        inventoryProduct.getMember(), inventoryProduct.getProduct())
                .orElseThrow(ReviewNotFoundException::new);

        return ReviewWithProductResponse.from(review);
    }
}
