package com.woowacourse.f12.application.review;

import com.woowacourse.f12.application.product.ProductDeletedEvent;
import com.woowacourse.f12.domain.review.ReviewRepository;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class ReviewEventListener {

    private final ReviewRepository reviewRepository;

    public ReviewEventListener(final ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }

    @EventListener
    public void handle(final ProductDeletedEvent event) {
        reviewRepository.deleteByProductId(event.getProductId());
    }
}
