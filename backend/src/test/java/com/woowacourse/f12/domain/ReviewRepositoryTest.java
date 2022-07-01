package com.woowacourse.f12.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.springframework.data.domain.Sort.Order.desc;

import com.woowacourse.f12.config.JpaConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;

@DataJpaTest
@Import(JpaConfig.class)
class ReviewRepositoryTest {

    @Autowired
    private ReviewRepository reviewRepository;

    @Test
    void 특정_제품의_리뷰_목록을_최신순으로_페이징하여_조회한다() {
        // given
        Long productId = 1L;
        Pageable pageable = PageRequest.of(0, 1, Sort.by(desc("createdAt")));
        reviewRepository.save(리뷰_저장(1L, "내용1", 5));
        Review review = reviewRepository.save(리뷰_저장(1L, "내용2", 5));

        // when
        Slice<Review> page = reviewRepository.findPageByProductId(productId, pageable);

        // then
        assertAll(
                () -> assertThat(page).hasSize(1)
                        .extracting("id")
                        .containsOnly(review.getId()),
                () -> assertThat(page.hasNext()).isTrue()
        );
    }

    @Test
    void 특정_제품의_리뷰_목록을_평점순으로_페이징하여_조회한다() {
        // given
        Long productId = 1L;
        Pageable pageable = PageRequest.of(0, 1, Sort.by(desc("rating")));
        Review review = reviewRepository.save(리뷰_저장(1L, "내용1", 5));
        reviewRepository.save(리뷰_저장(1L, "내용2", 4));

        // when
        Slice<Review> page = reviewRepository.findPageByProductId(productId, pageable);

        // then
        assertAll(
                () -> assertThat(page).hasSize(1)
                        .extracting("id")
                        .containsOnly(review.getId()),
                () -> assertThat(page.hasNext()).isTrue()
        );
    }

    @Test
    void 리뷰_목록을_최신순으로_페이징하여_조회한다() {
        // given
        Long productId = 1L;
        Pageable pageable = PageRequest.of(0, 1, Sort.by(desc("createdAt")));
        reviewRepository.save(리뷰_저장(1L, "내용1", 5));
        Review review = reviewRepository.save(리뷰_저장(2L, "내용2", 4));

        // when
        Slice<Review> page = reviewRepository.findPageBy(pageable);

        // then
        assertAll(
                () -> assertThat(page).hasSize(1)
                        .extracting("id")
                        .containsOnly(review.getId()),
                () -> assertThat(page.hasNext()).isTrue()
        );
    }

    private Review 리뷰_저장(Long productId, String content, int rating) {
        return Review.builder()
                .productId(productId)
                .content(content)
                .rating(rating)
                .build();
    }
}
