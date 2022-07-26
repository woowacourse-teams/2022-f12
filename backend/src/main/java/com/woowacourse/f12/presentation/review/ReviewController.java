package com.woowacourse.f12.presentation.review;

import com.woowacourse.f12.application.review.ReviewService;
import com.woowacourse.f12.dto.request.review.ReviewRequest;
import com.woowacourse.f12.dto.response.review.ReviewPageResponse;
import com.woowacourse.f12.dto.response.review.ReviewWithProductPageResponse;
import com.woowacourse.f12.presentation.auth.LoginRequired;
import com.woowacourse.f12.presentation.auth.VerifiedMember;
import java.net.URI;
import javax.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class ReviewController {

    private final ReviewService reviewService;

    public ReviewController(final ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @PostMapping("/keyboards/{productId}/reviews")
    @LoginRequired
    public ResponseEntity<Void> create(@PathVariable final Long productId,
                                       @VerifiedMember final Long memberId,
                                       @Valid @RequestBody final ReviewRequest reviewRequest) {
        final Long id = reviewService.saveReviewAndInventoryProduct(productId, memberId, reviewRequest);
        return ResponseEntity.created(URI.create("/api/v1/reviews/" + id))
                .build();
    }

    @GetMapping("/keyboards/{productId}/reviews")
    public ResponseEntity<ReviewPageResponse> showPageByProductId(@PathVariable final Long productId,
                                                                  final Pageable pageable) {
        final ReviewPageResponse reviewPageResponse = reviewService.findPageByProductId(productId, pageable);
        return ResponseEntity.ok(reviewPageResponse);
    }

    @GetMapping("/reviews")
    public ResponseEntity<ReviewWithProductPageResponse> showPage(final Pageable pageable) {
        final ReviewWithProductPageResponse reviewWithProductPageResponse = reviewService.findPage(pageable);
        return ResponseEntity.ok(reviewWithProductPageResponse);
    }

    @PutMapping("/reviews/{reviewId}")
    @LoginRequired
    public ResponseEntity<Void> update(@PathVariable final Long reviewId,
                                       @VerifiedMember final Long memberId,
                                       @Valid @RequestBody final ReviewRequest updateRequest) {
        reviewService.update(reviewId, memberId, updateRequest);
        return ResponseEntity.noContent()
                .build();
    }

    @DeleteMapping("/reviews/{reviewId}")
    @LoginRequired
    public ResponseEntity<Void> delete(@PathVariable final Long reviewId, @VerifiedMember final Long memberId) {
        reviewService.delete(reviewId, memberId);
        return ResponseEntity.noContent()
                .build();
    }
}
