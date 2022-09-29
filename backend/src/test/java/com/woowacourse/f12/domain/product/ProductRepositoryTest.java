package com.woowacourse.f12.domain.product;

import com.woowacourse.f12.config.JpaConfig;
import com.woowacourse.f12.domain.member.Member;
import com.woowacourse.f12.domain.member.MemberRepository;
import com.woowacourse.f12.domain.review.Review;
import com.woowacourse.f12.domain.review.ReviewRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;

import static com.woowacourse.f12.domain.product.Category.KEYBOARD;
import static com.woowacourse.f12.support.fixture.MemberFixture.CORINNE;
import static com.woowacourse.f12.support.fixture.MemberFixture.MINCHO;
import static com.woowacourse.f12.support.fixture.ProductFixture.*;
import static com.woowacourse.f12.support.fixture.ReviewFixture.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DataJpaTest
@Import(JpaConfig.class)
class ProductRepositoryTest {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    @Test
    void 제품을_단일_조회_한다() {
        // given
        Product product = 제품_저장(KEYBOARD_1.생성());
        Member member1 = memberRepository.save(CORINNE.생성());
        Member member2 = memberRepository.save(MINCHO.생성());
        Review review1 = REVIEW_RATING_4.작성(product, member1);
        Review review2 = REVIEW_RATING_5.작성(product, member2);

        리뷰_저장(review1);
        리뷰_저장(review2);

        // when
        Product savedProduct = productRepository.findById(product.getId())
                .orElseThrow(IllegalArgumentException::new);

        // then
        assertAll(
                () -> assertThat(savedProduct.getRating()).isEqualTo(4.5),
                () -> assertThat(savedProduct.getReviewCount()).isEqualTo(2)
        );
    }

    @Test
    void 키보드_전체_목록을_페이징하여_조회한다() {
        // given
        Product product1 = 제품_저장(KEYBOARD_1.생성());
        제품_저장(KEYBOARD_2.생성());
        Pageable pageable = PageRequest.of(0, 1);

        // when
        Slice<Product> slice = productRepository.findWithSearchConditions(null, KEYBOARD, pageable);

        // then
        assertAll(
                () -> assertThat(slice.hasNext()).isTrue(),
                () -> assertThat(slice.getContent()).containsExactly(product1)
        );
    }

    @Test
    void 키보드_전체_목록을_리뷰_많은_순으로_페이징하여_조회한다() {
        // given
        Product product1 = 제품_저장(KEYBOARD_1.생성());
        Product product2 = 제품_저장(KEYBOARD_2.생성());
        Member member1 = memberRepository.save(CORINNE.생성());
        Member member2 = memberRepository.save(MINCHO.생성());

        Review review1 = REVIEW_RATING_5.작성(product1, member1);
        Review review2 = REVIEW_RATING_5.작성(product2, member1);
        Review review3 = REVIEW_RATING_5.작성(product2, member2);

        리뷰_저장(review1);
        리뷰_저장(review2);
        리뷰_저장(review3);

        Pageable pageable = PageRequest.of(0, 1, Sort.by(Order.desc("reviewCount")));

        // when
        Slice<Product> slice = productRepository.findWithSearchConditions(null, KEYBOARD, pageable);

        // then
        assertAll(
                () -> assertThat(slice.hasNext()).isTrue(),
                () -> assertThat(slice.getContent()).containsExactly(product2)
        );
    }

    @Test
    void 키보드_전체_목록을_평균_평점_순으로_페이징하여_조회한다() {
        // given
        Product product1 = 제품_저장(KEYBOARD_1.생성());
        Product product2 = 제품_저장(KEYBOARD_2.생성());
        Product product3 = 제품_저장(MOUSE_1.생성());
        Product product4 = 제품_저장(MOUSE_2.생성());
        Member member = memberRepository.save(CORINNE.생성());

        리뷰_저장(REVIEW_RATING_2.작성(product1, member));
        리뷰_저장(REVIEW_RATING_1.작성(product2, member));
        리뷰_저장(REVIEW_RATING_5.작성(product3, member));
        리뷰_저장(REVIEW_RATING_4.작성(product4, member));

        Pageable pageable = PageRequest.of(0, 1, Sort.by(Order.desc("rating")));

        // when
        Slice<Product> slice = productRepository.findWithSearchConditions(null, KEYBOARD, pageable);

        // then
        assertAll(
                () -> assertThat(slice.hasNext()).isTrue(),
                () -> assertThat(slice.getContent()).containsExactly(product1)
        );
    }

    @Test
    void 제품명으로_제품_목록을_조회한다() {
        // given
        Product keyboard1 = 제품_저장(KEYBOARD_1.생성());
        제품_저장(KEYBOARD_2.생성());
        Product mouse1 = 제품_저장(MOUSE_1.생성());
        Pageable pageable = PageRequest.of(0, 2);

        // when
        Slice<Product> page = productRepository.findWithSearchConditions("1", null, pageable);

        // then
        assertAll(
                () -> assertThat(page.hasNext()).isFalse(),
                () -> assertThat(page.getContent()).contains(mouse1, keyboard1)
        );
    }

    @Test
    void 제품명과_카테고리로_제품_목록을_조회한다() {
        // given
        Product keyboard1 = 제품_저장(KEYBOARD_1.생성());
        제품_저장(KEYBOARD_2.생성());
        제품_저장(MOUSE_1.생성());
        Pageable pageable = PageRequest.of(0, 2);

        // when
        Slice<Product> page = productRepository.findWithSearchConditions("1", KEYBOARD, pageable);

        // then
        assertAll(
                () -> assertThat(page.hasNext()).isFalse(),
                () -> assertThat(page.getContent()).contains(keyboard1)
        );
    }

    @Test
    void 제품들을_where_조건없이_조회한다() {
        // given
        Product keyboard1 = 제품_저장(KEYBOARD_1.생성());
        Member member = memberRepository.save(CORINNE.생성());
        제품_저장(KEYBOARD_2.생성());
        리뷰_저장(REVIEW_RATING_5.작성(keyboard1, member));

        Pageable pageable = PageRequest.of(0, 1, Sort.by(Order.desc("rating"), Order.desc("reviewCount")));

        // when
        Slice<Product> page = productRepository.findWithoutSearchConditions(pageable);

        // then
        assertAll(
                () -> assertThat(page.hasNext()).isTrue(),
                () -> assertThat(page.getContent()).containsOnly(keyboard1)
        );
    }

    private Product 제품_저장(Product product) {
        return productRepository.save(product);
    }

    private Review 리뷰_저장(Review review) {
        review.reflectToProductWhenWritten();
        return reviewRepository.save(review);
    }
}
