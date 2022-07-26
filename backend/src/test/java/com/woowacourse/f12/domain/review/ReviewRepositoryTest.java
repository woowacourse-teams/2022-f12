package com.woowacourse.f12.domain.review;

import static com.woowacourse.f12.support.ProductFixture.KEYBOARD_1;
import static com.woowacourse.f12.support.ProductFixture.KEYBOARD_2;
import static com.woowacourse.f12.support.MemberFixtures.CORINNE;
import static com.woowacourse.f12.support.ReviewFixtures.REVIEW_RATING_4;
import static com.woowacourse.f12.support.ReviewFixtures.REVIEW_RATING_5;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.springframework.data.domain.Sort.Order.desc;

import com.woowacourse.f12.config.JpaConfig;
import com.woowacourse.f12.domain.member.Member;
import com.woowacourse.f12.domain.member.MemberRepository;
import com.woowacourse.f12.domain.product.Product;
import com.woowacourse.f12.domain.product.ProductRepository;
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

@DataJpaTest
@Import(JpaConfig.class)
class ReviewRepositoryTest {

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private MemberRepository memberRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Test
    void 특정_제품의_리뷰_목록을_최신순으로_페이징하여_조회한다() {
        // given
        Product product = productRepository.save(KEYBOARD_1.생성());
        Member member = memberRepository.save(CORINNE.생성(1L));
        Pageable pageable = PageRequest.of(0, 1, Sort.by(desc("createdAt")));
        리뷰_저장(REVIEW_RATING_5.작성(product, member));
        Review review = 리뷰_저장(REVIEW_RATING_5.작성(product, member));

        // when
        Slice<Review> page = reviewRepository.findPageByProductId(product.getId(), pageable);

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
        Product product = productRepository.save(KEYBOARD_1.생성());
        Member member = memberRepository.save(CORINNE.생성(1L));
        Pageable pageable = PageRequest.of(0, 1, Sort.by(desc("rating")));
        Review review = 리뷰_저장(REVIEW_RATING_5.작성(product, member));
        리뷰_저장(REVIEW_RATING_4.작성(product, member));

        // when
        Slice<Review> page = reviewRepository.findPageByProductId(product.getId(), pageable);

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
        Product product1 = productRepository.save(KEYBOARD_1.생성());
        Product product2 = productRepository.save(KEYBOARD_2.생성());
        Member member = memberRepository.save(CORINNE.생성(1L));
        Pageable pageable = PageRequest.of(0, 1, Sort.by(desc("createdAt")));
        리뷰_저장(REVIEW_RATING_5.작성(product1, member));
        Review review = 리뷰_저장(REVIEW_RATING_5.작성(product2, member));

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

    @Test
    void 프록시의_equals_hashCode_테스트() {
        // given
        Product product = productRepository.save(KEYBOARD_1.생성());
        Member member = memberRepository.save(CORINNE.생성());
        Long testReviewId = reviewRepository.save(REVIEW_RATING_5.작성(product, member))
                .getId();
        Long testKeyboardId = product.getId();
        Long testMemberId = member.getId();
        entityManager.clear();

        // when
        Review review = reviewRepository.findById(testReviewId)
                .get();
        Member targetMember = memberRepository.findById(testMemberId)
                .get();
        Product targetProduct = productRepository.findById(testKeyboardId)
                .get();

        // then
        assertAll(
                () -> assertThat(targetMember).isEqualTo(review.getMember()),
                () -> assertThat(targetProduct).isEqualTo(review.getProduct())
        );
    }

    @Test
    void 회원과_제품으로_작성된_리뷰가_존재하는지_확인한다() {
        // given
        Product product = productRepository.save(KEYBOARD_1.생성());
        Member member = memberRepository.save(CORINNE.생성());
        reviewRepository.save(REVIEW_RATING_5.작성(product, member));

        // when
        boolean actual = reviewRepository.existsByMemberAndProduct(member, product);

        // then
        assertThat(actual).isTrue();
    }

    private Review 리뷰_저장(Review review) {
        return reviewRepository.save(review);
    }
}
