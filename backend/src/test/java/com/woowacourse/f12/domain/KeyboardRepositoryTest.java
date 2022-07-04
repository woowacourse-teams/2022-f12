package com.woowacourse.f12.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.woowacourse.f12.config.JpaConfig;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;

@DataJpaTest
@Import(JpaConfig.class)
class KeyboardRepositoryTest {

    @Autowired
    private KeyboardRepository keyboardRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Test
    void 키보드를_단일_조회_한다() {
        // given
        Keyboard keyboard = 키보드_저장("키보드");
        리뷰_저장(keyboard.getId(), "리뷰내용", 4);
        리뷰_저장(keyboard.getId(), "리뷰내용", 5);
        entityManager.flush();
        entityManager.refresh(keyboard);

        // when
        Keyboard savedKeyboard = keyboardRepository.findById(keyboard.getId())
                .orElseThrow(IllegalArgumentException::new);

        // then
        assertAll(
                () -> assertThat(savedKeyboard.getRating()).isEqualTo(4.5),
                () -> assertThat(savedKeyboard.getReviewCount()).isEqualTo(2)
        );
    }

    @Test
    void 키보드_전체_목록을_페이징하여_조회한다() {
        // given
        Keyboard keyboard1 = 키보드_저장("키보드1");
        Keyboard keyboard2 = 키보드_저장("키보드2");
        Pageable pageable = PageRequest.of(0, 1);

        // when
        Slice<Keyboard> slice = keyboardRepository.findPageBy(pageable);

        // then
        assertAll(
                () -> assertThat(slice.hasNext()).isTrue(),
                () -> assertThat(slice.getContent()).containsExactly(keyboard1)
        );
    }

    @Test
    void 키보드_전체_목록을_리뷰_많은_순으로_페이징하여_조회한다() {
        // given
        Keyboard keyboard1 = 키보드_저장("키보드1");
        Keyboard keyboard2 = 키보드_저장("키보드2");

        리뷰_저장(keyboard1.getId(), "내용", 5);
        리뷰_저장(keyboard2.getId(), "내용", 5);
        리뷰_저장(keyboard2.getId(), "내용", 5);

        Pageable pageable = PageRequest.of(0, 1, Sort.by(Order.desc("reviewCount")));

        // when
        Slice<Keyboard> slice = keyboardRepository.findPageBy(pageable);

        // then
        assertAll(
                () -> assertThat(slice.hasNext()).isTrue(),
                () -> assertThat(slice.getContent()).containsExactly(keyboard2)
        );
    }

    @Test
    void 키보드_전체_목록을_평균_평점_순으로_페이징하여_조회한다() {
        // given
        Keyboard keyboard1 = 키보드_저장("키보드1");
        Keyboard keyboard2 = 키보드_저장("키보드2");

        리뷰_저장(keyboard1.getId(), "내용", 2);
        리뷰_저장(keyboard1.getId(), "내용", 1);
        리뷰_저장(keyboard2.getId(), "내용", 5);
        리뷰_저장(keyboard2.getId(), "내용", 4);

        Pageable pageable = PageRequest.of(0, 1, Sort.by(Order.desc("rating")));

        // when
        Slice<Keyboard> slice = keyboardRepository.findPageBy(pageable);

        // then
        assertAll(
                () -> assertThat(slice.hasNext()).isTrue(),
                () -> assertThat(slice.getContent()).containsExactly(keyboard2)
        );
    }

    private Keyboard 키보드_저장(String name) {
        return keyboardRepository.save(Keyboard.builder()
                .name(name)
                .build()
        );
    }

    private Review 리뷰_저장(Long productId, String content, int rating) {
        return reviewRepository.save(Review.builder()
                .productId(productId)
                .content(content)
                .rating(rating)
                .build());
    }
}
