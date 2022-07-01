package com.woowacourse.f12.presentation;

import com.woowacourse.f12.application.ReviewService;
import com.woowacourse.f12.dto.request.ReviewRequest;
import com.woowacourse.f12.dto.response.ReviewPageResponse;
import java.net.URI;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/keyboards")
public class ReviewController {

    private final ReviewService reviewService;

    public ReviewController(final ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @PostMapping("/{productId}/reviews")
    public ResponseEntity<Void> create(@PathVariable final Long productId,
                                       @RequestBody final ReviewRequest reviewRequest) {
        final Long id = reviewService.save(productId, reviewRequest);
        return ResponseEntity.created(URI.create("/api/v1/reviews/" + id))
                .build();
    }

    @GetMapping("/{productId}/reviews")
    public ResponseEntity<ReviewPageResponse> showPageByProductId(@PathVariable final Long productId,
                                                                  final Pageable pageable) {
        final ReviewPageResponse reviewPageResponse = reviewService.findPageByProductId(productId, pageable);
        return ResponseEntity.ok(reviewPageResponse);
    }

    @GetMapping("/reviews")
    public ResponseEntity<ReviewPageResponse> showPage(final Pageable pageable) {
        final ReviewPageResponse reviewPageResponse = reviewService.findPage(pageable);
        return ResponseEntity.ok(reviewPageResponse);
    }
}
