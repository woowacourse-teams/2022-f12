package com.woowacourse.f12.presentation.review;

import com.woowacourse.f12.application.auth.token.MemberPayload;
import com.woowacourse.f12.application.review.ReviewService;
import com.woowacourse.f12.dto.request.review.ReviewRequest;
import com.woowacourse.f12.dto.response.review.ReviewWithAuthorAndProductPageResponse;
import com.woowacourse.f12.dto.response.review.ReviewWithAuthorPageResponse;
import com.woowacourse.f12.dto.response.review.ReviewWithProductPageResponse;
import com.woowacourse.f12.dto.response.review.ReviewWithProductResponse;
import com.woowacourse.f12.presentation.auth.Login;
import com.woowacourse.f12.presentation.auth.VerifiedMember;
import com.woowacourse.f12.support.CursorPageable;
import com.woowacourse.f12.support.MemberPayloadSupport;
import java.net.URI;
import javax.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.SortDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class ReviewController {

    private final ReviewService reviewService;

    public ReviewController(final ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @PostMapping("/products/{productId}/reviews")
    @Login
    public ResponseEntity<Void> create(@PathVariable final Long productId,
                                       @VerifiedMember final MemberPayload memberPayload,
                                       @Valid @RequestBody final ReviewRequest reviewRequest) {
        final Long id = reviewService.saveReviewAndInventoryProduct(productId, memberPayload.getId(), reviewRequest);
        return ResponseEntity.created(URI.create("/api/v1/reviews/" + id))
                .build();
    }

    @GetMapping("/products/{productId}/reviews")
    @Login(required = false)
    public ResponseEntity<ReviewWithAuthorPageResponse> showPageByProductId(@PathVariable final Long productId,
                                                                            @VerifiedMember @Nullable MemberPayload memberPayload,
                                                                            final Pageable pageable) {
        final Long loggedInMemberId = MemberPayloadSupport.getLoggedInMemberId(memberPayload);
        final ReviewWithAuthorPageResponse reviewPageResponse = reviewService.findPageByProductId(productId,
                loggedInMemberId, pageable);
        return ResponseEntity.ok(reviewPageResponse);
    }

    @GetMapping(value = "/reviews")
    public ResponseEntity<ReviewWithAuthorAndProductPageResponse> showPageByCursor(
            @RequestParam(required = false) final Long cursor, final Integer size,
            @SortDefault(sort = "id", direction = Direction.DESC) final Sort sort) {
        final CursorPageable cursorPageable = new CursorPageable(cursor, size, sort);
        final ReviewWithAuthorAndProductPageResponse reviewWithAuthorAndProductPageResponse
                = reviewService.findPage(cursorPageable);
        return ResponseEntity.ok(reviewWithAuthorAndProductPageResponse);
    }

    @PutMapping("/reviews/{reviewId}")
    @Login
    public ResponseEntity<Void> update(@PathVariable final Long reviewId,
                                       @VerifiedMember final MemberPayload memberPayload,
                                       @Valid @RequestBody final ReviewRequest updateRequest) {
        reviewService.update(reviewId, memberPayload.getId(), updateRequest);
        return ResponseEntity.noContent()
                .build();
    }

    @DeleteMapping("/reviews/{reviewId}")
    @Login
    public ResponseEntity<Void> delete(@PathVariable final Long reviewId,
                                       @VerifiedMember final MemberPayload memberPayload) {
        reviewService.delete(reviewId, memberPayload.getId());
        return ResponseEntity.noContent()
                .build();
    }

    @GetMapping("/members/{memberId}/reviews")
    public ResponseEntity<ReviewWithProductPageResponse> showPageByMemberId(@PathVariable final Long memberId,
                                                                            final Pageable pageable) {
        final ReviewWithProductPageResponse reviewWithProductPageResponse = reviewService.findPageByMemberId(memberId,
                pageable);
        return ResponseEntity.ok(reviewWithProductPageResponse);
    }

    @GetMapping("/members/me/reviews")
    @Login
    public ResponseEntity<ReviewWithProductPageResponse> showMyReviewPage(
            @VerifiedMember final MemberPayload memberPayload,
            final Pageable pageable) {
        final ReviewWithProductPageResponse reviewWithProductPageResponse = reviewService.findPageByMemberId(
                memberPayload.getId(), pageable);
        return ResponseEntity.ok(reviewWithProductPageResponse);
    }

    @GetMapping("/inventoryProducts/{inventoryProductId}/reviews")
    public ResponseEntity<ReviewWithProductResponse> showReview(@PathVariable final Long inventoryProductId) {
        final ReviewWithProductResponse reviewResponse = reviewService.findByInventoryProductId(inventoryProductId);
        return ResponseEntity.ok(reviewResponse);
    }
}
