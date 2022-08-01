package com.woowacourse.f12.application.product;

import static com.woowacourse.f12.domain.member.CareerLevel.JUNIOR;
import static com.woowacourse.f12.domain.member.CareerLevel.MID_LEVEL;
import static com.woowacourse.f12.domain.member.JobType.BACKEND;
import static com.woowacourse.f12.domain.member.JobType.ETC;
import static com.woowacourse.f12.presentation.member.CareerLevelConstant.JUNIOR_CONSTANT;
import static com.woowacourse.f12.presentation.member.CareerLevelConstant.MID_LEVEL_CONSTANT;
import static com.woowacourse.f12.presentation.member.CareerLevelConstant.NONE_CONSTANT;
import static com.woowacourse.f12.presentation.member.CareerLevelConstant.SENIOR_CONSTANT;
import static com.woowacourse.f12.presentation.member.JobTypeConstant.BACKEND_CONSTANT;
import static com.woowacourse.f12.presentation.member.JobTypeConstant.ETC_CONSTANT;
import static com.woowacourse.f12.presentation.member.JobTypeConstant.FRONTEND_CONSTANT;
import static com.woowacourse.f12.presentation.member.JobTypeConstant.MOBILE_CONSTANT;
import static com.woowacourse.f12.support.ProductFixture.KEYBOARD_1;
import static com.woowacourse.f12.support.ProductFixture.MOUSE_1;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import com.woowacourse.f12.domain.product.Category;
import com.woowacourse.f12.domain.product.Product;
import com.woowacourse.f12.domain.product.ProductRepository;
import com.woowacourse.f12.domain.review.CareerLevelCount;
import com.woowacourse.f12.domain.review.JobTypeCount;
import com.woowacourse.f12.domain.review.ReviewRepository;
import com.woowacourse.f12.dto.response.product.ProductPageResponse;
import com.woowacourse.f12.dto.response.product.ProductResponse;
import com.woowacourse.f12.dto.response.product.ProductStatisticsResponse;
import com.woowacourse.f12.exception.notfound.ProductNotFoundException;
import com.woowacourse.f12.presentation.product.CategoryConstant;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.SliceImpl;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ReviewRepository reviewRepository;

    @InjectMocks
    private ProductService productService;

    @Test
    void id_값으로_제품을_조회한다() {
        // given
        Product product = KEYBOARD_1.생성(1L);

        given(productRepository.findById(anyLong()))
                .willReturn(Optional.of(product));

        // when
        ProductResponse productResponse = productService.findById(1L);

        // then
        assertAll(
                () -> verify(productRepository).findById(1L),
                () -> assertThat(productResponse).usingRecursiveComparison()
                        .isEqualTo(ProductResponse.from(product))
        );
    }

    @Test
    void 존재하지_않는_id_값으로_제품을_조회하면_예외를_반환한다() {
        // given
        given(productRepository.findById(anyLong()))
                .willReturn(Optional.empty());

        // when then
        assertAll(
                () -> assertThatThrownBy(() -> productService.findById(1L))
                        .isExactlyInstanceOf(ProductNotFoundException.class),
                () -> verify(productRepository).findById(1L)
        );
    }

    @Test
    void 전체_키보드_목록을_조회한다() {
        // given
        Product product = KEYBOARD_1.생성(1L);
        Pageable pageable = PageRequest.of(0, 1);
        given(productRepository.findPageByCategory(eq(Category.KEYBOARD), any(Pageable.class)))
                .willReturn(new SliceImpl<>(List.of(product), pageable, false));

        // when
        ProductPageResponse productPageResponse = productService.findPage(CategoryConstant.KEYBOARD_CONSTANT, pageable);

        // then
        assertAll(
                () -> verify(productRepository).findPageByCategory(eq(Category.KEYBOARD), any(Pageable.class)),
                () -> assertThat(productPageResponse.isHasNext()).isFalse(),
                () -> assertThat(productPageResponse.getItems()).hasSize(1)
                        .usingRecursiveFieldByFieldElementComparator()
                        .containsOnly(ProductResponse.from(product))
        );
    }

    @Test
    void 전체_제품_목록을_조회한다() {
        // given
        Product product = KEYBOARD_1.생성(1L);
        MOUSE_1.생성(2L);
        Pageable pageable = PageRequest.of(0, 1);
        given(productRepository.findPageBy(any(Pageable.class)))
                .willReturn(new SliceImpl<>(List.of(product), pageable, true));

        // when
        ProductPageResponse productPageResponse = productService.findPage(null, pageable);

        // then
        assertAll(
                () -> verify(productRepository).findPageBy(any(Pageable.class)),
                () -> assertThat(productPageResponse.isHasNext()).isTrue(),
                () -> assertThat(productPageResponse.getItems()).hasSize(1)
                        .usingRecursiveFieldByFieldElementComparator()
                        .containsOnly(ProductResponse.from(product))
        );
    }

    @Test
    void 특정_제품의_사용자의_연차와_직군의_비율을_반환한다() {
        // given
        Long productId = 1L;
        given(productRepository.existsById(productId))
                .willReturn(true);
        given(reviewRepository.findCareerLevelCountByProductId(productId))
                .willReturn(List.of(new CareerLevelCount(JUNIOR, 1), new CareerLevelCount(MID_LEVEL, 1)));
        given(reviewRepository.findJobTypeCountByProductId(productId))
                .willReturn(List.of(new JobTypeCount(BACKEND, 1), new JobTypeCount(ETC, 1)));

        // when
        ProductStatisticsResponse productStatisticsResponse = productService.calculateMemberStatisticsById(productId);

        // then
        assertAll(
                () -> verify(productRepository).existsById(productId),
                () -> verify(reviewRepository).findCareerLevelCountByProductId(productId),
                () -> verify(reviewRepository).findJobTypeCountByProductId(productId),
                () -> assertThat(productStatisticsResponse.getCareerLevel()).usingRecursiveComparison()
                        .isEqualTo(Map.of(NONE_CONSTANT, 0.0, JUNIOR_CONSTANT, 0.5, MID_LEVEL_CONSTANT, 0.5,
                                SENIOR_CONSTANT, 0.0)),
                () -> assertThat(productStatisticsResponse.getJobType()).usingRecursiveComparison()
                        .isEqualTo(Map.of(FRONTEND_CONSTANT, 0.0, BACKEND_CONSTANT, 0.5, MOBILE_CONSTANT, 0.0,
                                ETC_CONSTANT, 0.5))
        );
    }
}
