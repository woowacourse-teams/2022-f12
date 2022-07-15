package com.woowacourse.f12.domain;

import static com.woowacourse.f12.support.KeyboardFixtures.KEYBOARD_1;
import static com.woowacourse.f12.support.KeyboardFixtures.KEYBOARD_2;
import static com.woowacourse.f12.support.ReviewFixtures.REVIEW_RATING_1;
import static com.woowacourse.f12.support.ReviewFixtures.REVIEW_RATING_2;
import static com.woowacourse.f12.support.ReviewFixtures.REVIEW_RATING_4;
import static com.woowacourse.f12.support.ReviewFixtures.REVIEW_RATING_5;
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
        Keyboard keyboard = 키보드_저장(KEYBOARD_1.생성());
        리뷰_저장(REVIEW_RATING_4.작성(keyboard));
        리뷰_저장(REVIEW_RATING_5.작성(keyboard));
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
        Keyboard keyboard1 = 키보드_저장(KEYBOARD_1.생성());
        키보드_저장(KEYBOARD_2.생성());
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
        Keyboard keyboard1 = 키보드_저장(KEYBOARD_1.생성());
        Keyboard keyboard2 = 키보드_저장(KEYBOARD_2.생성());

        리뷰_저장(REVIEW_RATING_5.작성(keyboard1));
        리뷰_저장(REVIEW_RATING_5.작성(keyboard2));
        리뷰_저장(REVIEW_RATING_5.작성(keyboard2));

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
        Keyboard keyboard2 = 키보드_저장(KEYBOARD_1.생성());
        Keyboard keyboard1 = 키보드_저장(KEYBOARD_2.생성());

        리뷰_저장(REVIEW_RATING_2.작성(keyboard1));
        리뷰_저장(REVIEW_RATING_1.작성(keyboard1));
        리뷰_저장(REVIEW_RATING_5.작성(keyboard2));
        리뷰_저장(REVIEW_RATING_4.작성(keyboard2));

        Pageable pageable = PageRequest.of(0, 1, Sort.by(Order.desc("rating")));

        // when
        Slice<Keyboard> slice = keyboardRepository.findPageBy(pageable);

        // then
        assertAll(
                () -> assertThat(slice.hasNext()).isTrue(),
                () -> assertThat(slice.getContent()).containsExactly(keyboard2)
        );
    }

    private Keyboard 키보드_저장(Keyboard keyboard) {
        return keyboardRepository.save(keyboard);
    }

    private Review 리뷰_저장(Review review) {
        return reviewRepository.save(review);
    }
}
