package com.woowacourse.f12.presentation;

import com.woowacourse.f12.application.ReviewService;
import com.woowacourse.f12.dto.request.ReviewRequest;
import java.net.URI;
import org.springframework.http.ResponseEntity;
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
}
