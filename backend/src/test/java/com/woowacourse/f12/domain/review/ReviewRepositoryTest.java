package com.woowacourse.f12.domain.review;

import static com.woowacourse.f12.domain.member.CareerLevel.JUNIOR;
import static com.woowacourse.f12.domain.member.CareerLevel.SENIOR;
import static com.woowacourse.f12.domain.member.JobType.BACKEND;
import static com.woowacourse.f12.domain.member.JobType.MOBILE;
import static com.woowacourse.f12.support.fixture.MemberFixture.CORINNE;
import static com.woowacourse.f12.support.fixture.MemberFixture.MINCHO;
import static com.woowacourse.f12.support.fixture.MemberFixture.NOT_ADDITIONAL_INFO;
import static com.woowacourse.f12.support.fixture.ProductFixture.KEYBOARD_1;
import static com.woowacourse.f12.support.fixture.ProductFixture.KEYBOARD_2;
import static com.woowacourse.f12.support.fixture.ReviewFixture.REVIEW_RATING_1;
import static com.woowacourse.f12.support.fixture.ReviewFixture.REVIEW_RATING_2;
import static com.woowacourse.f12.support.fixture.ReviewFixture.REVIEW_RATING_4;
import static com.woowacourse.f12.support.fixture.ReviewFixture.REVIEW_RATING_5;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.springframework.data.domain.Sort.Order.desc;

import com.woowacourse.f12.domain.RepositoryTest;
import com.woowacourse.f12.domain.member.Member;
import com.woowacourse.f12.domain.member.MemberRepository;
import com.woowacourse.f12.domain.product.Product;
import com.woowacourse.f12.domain.product.ProductRepository;
import java.util.List;
import java.util.Optional;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;

@RepositoryTest
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
        Member member = memberRepository.save(CORINNE.생성());
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
        Member member = memberRepository.save(CORINNE.생성());
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
        Member member = memberRepository.save(CORINNE.생성());
        Pageable pageable = PageRequest.of(0, 1, Sort.by(desc("id")));
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

    @Test
    void 제품에_대한_사용자_연차의_총_개수를_반환한다() {
        // given
        Product product = 제품_저장(KEYBOARD_1.생성());

        Member corinne = CORINNE.추가정보를_입력하여_생성(SENIOR, MOBILE);
        corinne = memberRepository.save(corinne);

        Member mincho = MINCHO.추가정보를_입력하여_생성(JUNIOR, BACKEND);
        mincho = memberRepository.save(mincho);

        리뷰_저장(REVIEW_RATING_2.작성(product, corinne));
        리뷰_저장(REVIEW_RATING_1.작성(product, mincho));

        // when
        List<CareerLevelCount> careerLevelCounts = reviewRepository.findCareerLevelCountByProductId(
                product.getId());

        // then
        assertThat(careerLevelCounts).usingRecursiveFieldByFieldElementComparator()
                .hasSize(2)
                .containsOnly(
                        new CareerLevelCount(JUNIOR, 1),
                        new CareerLevelCount(SENIOR, 1)
                );
    }

    @Test
    void 제품에_대한_사용자_연차의_총_개수를_반환할때_추가정보가_없는_회원은_포함되지_않는다() {
        // given
        Product product = 제품_저장(KEYBOARD_1.생성());

        Member corinne = CORINNE.생성();
        corinne = memberRepository.save(corinne);
        Member notAdditionalInfo = NOT_ADDITIONAL_INFO.생성();
        notAdditionalInfo = memberRepository.save(notAdditionalInfo);

        리뷰_저장(REVIEW_RATING_2.작성(product, corinne));
        리뷰_저장(REVIEW_RATING_1.작성(product, notAdditionalInfo));

        // when
        List<CareerLevelCount> careerLevelCounts = reviewRepository.findCareerLevelCountByProductId(
                product.getId());

        // then
        assertThat(careerLevelCounts).usingRecursiveFieldByFieldElementComparator()
                .hasSize(1)
                .containsOnly(new CareerLevelCount(SENIOR, 1));
    }

    @Test
    void 제품에_대한_사용자_직군의_총_개수를_반환한다() {
        // given
        Product product = 제품_저장(KEYBOARD_1.생성());

        Member corinne = CORINNE.추가정보를_입력하여_생성(SENIOR, MOBILE);
        corinne = memberRepository.save(corinne);

        Member mincho = MINCHO.추가정보를_입력하여_생성(JUNIOR, BACKEND);
        mincho = memberRepository.save(mincho);

        리뷰_저장(REVIEW_RATING_2.작성(product, corinne));
        리뷰_저장(REVIEW_RATING_1.작성(product, mincho));

        // when
        List<JobTypeCount> jobTypeCounts = reviewRepository.findJobTypeCountByProductId(product.getId());

        // then
        assertThat(jobTypeCounts).usingRecursiveFieldByFieldElementComparator()
                .hasSize(2)
                .containsOnly(
                        new JobTypeCount(MOBILE, 1),
                        new JobTypeCount(BACKEND, 1)
                );
    }

    @Test
    void 제품에_대한_사용자_직군의_총_개수를_반환할때_추가정보가_없는_회원은_포함되지_않는다() {
        // given
        Product product = 제품_저장(KEYBOARD_1.생성());

        Member corinne = CORINNE.생성();
        corinne = memberRepository.save(corinne);
        Member notAdditionalInfo = NOT_ADDITIONAL_INFO.생성();
        notAdditionalInfo = memberRepository.save(notAdditionalInfo);

        리뷰_저장(REVIEW_RATING_2.작성(product, corinne));
        리뷰_저장(REVIEW_RATING_1.작성(product, notAdditionalInfo));

        // when
        List<JobTypeCount> jobTypeCounts = reviewRepository.findJobTypeCountByProductId(product.getId());

        // then
        assertThat(jobTypeCounts).usingRecursiveFieldByFieldElementComparator()
                .hasSize(1)
                .containsOnly(new JobTypeCount(BACKEND, 1));
    }

    @Test
    void 멤버와_제품으로_리뷰를_조회한다() {
        // given
        Product product = 제품_저장(KEYBOARD_1.생성());
        Member corinne = memberRepository.save(CORINNE.생성());
        Review savedReview = 리뷰_저장(REVIEW_RATING_1.작성(product, corinne));

        // when
        Optional<Review> review = reviewRepository.findByMemberAndProduct(corinne, product);

        // then
        assertThat(review.get()).usingRecursiveComparison()
                .isEqualTo(savedReview);
    }

    @Test
    void 회원으로_리뷰목록을_조회한다() {
        // given
        Product product1 = 제품_저장(KEYBOARD_1.생성());
        Product product2 = 제품_저장(KEYBOARD_2.생성());
        Member corinne = memberRepository.save(CORINNE.생성());
        Review savedReview1 = 리뷰_저장(REVIEW_RATING_1.작성(product1, corinne));
        Review savedReview2 = 리뷰_저장(REVIEW_RATING_1.작성(product2, corinne));
        Pageable pageable = PageRequest.of(0, 2, Sort.by(desc("createdAt")));

        // when
        Slice<Review> page = reviewRepository.findPageByMemberId(corinne.getId(), pageable);

        // then
        assertAll(
                () -> assertThat(page.hasNext()).isFalse(),
                () -> assertThat(page.getContent()).usingRecursiveFieldByFieldElementComparator()
                        .containsExactly(savedReview2, savedReview1)
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
