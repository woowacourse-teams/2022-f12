package com.woowacourse.f12.domain.statistics;

import static com.woowacourse.f12.domain.member.CareerLevel.JUNIOR;
import static com.woowacourse.f12.domain.member.CareerLevel.SENIOR;
import static com.woowacourse.f12.domain.member.JobType.BACKEND;
import static com.woowacourse.f12.domain.member.JobType.MOBILE;
import static com.woowacourse.f12.support.fixture.MemberFixture.CORINNE;
import static com.woowacourse.f12.support.fixture.MemberFixture.MINCHO;
import static com.woowacourse.f12.support.fixture.MemberFixture.NOT_ADDITIONAL_INFO;
import static com.woowacourse.f12.support.fixture.ProductFixture.KEYBOARD_1;
import static com.woowacourse.f12.support.fixture.ReviewFixture.REVIEW_RATING_1;
import static com.woowacourse.f12.support.fixture.ReviewFixture.REVIEW_RATING_2;
import static org.assertj.core.api.Assertions.assertThat;

import com.woowacourse.f12.domain.RepositoryTest;
import com.woowacourse.f12.domain.member.Member;
import com.woowacourse.f12.domain.member.MemberRepository;
import com.woowacourse.f12.domain.product.Product;
import com.woowacourse.f12.domain.product.ProductRepository;
import com.woowacourse.f12.domain.review.Review;
import com.woowacourse.f12.domain.review.ReviewRepository;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;

@RepositoryTest
@Import(StatisticsRepositoryImpl.class)
public class StatisticsRepositoryTest {

    @Autowired
    private StatisticsRepository statisticsRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private MemberRepository memberRepository;

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
        List<CareerLevelCount> careerLevelCounts = statisticsRepository.findCareerLevelCountByProductId(
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
        List<CareerLevelCount> careerLevelCounts = statisticsRepository.findCareerLevelCountByProductId(
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
        List<JobTypeCount> jobTypeCounts = statisticsRepository.findJobTypeCountByProductId(product.getId());

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
        List<JobTypeCount> jobTypeCounts = statisticsRepository.findJobTypeCountByProductId(product.getId());

        // then
        assertThat(jobTypeCounts).usingRecursiveFieldByFieldElementComparator()
                .hasSize(1)
                .containsOnly(new JobTypeCount(BACKEND, 1));
    }

    private Product 제품_저장(Product product) {
        return productRepository.save(product);
    }

    private Review 리뷰_저장(Review review) {
        return reviewRepository.save(review);
    }
}
