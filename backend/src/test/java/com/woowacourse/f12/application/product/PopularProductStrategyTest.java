package com.woowacourse.f12.application.product;

import static org.assertj.core.api.Assertions.assertThat;

import com.woowacourse.f12.domain.product.Category;
import com.woowacourse.f12.domain.product.Product;
import com.woowacourse.f12.dto.response.PopularProductsResponse;
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class PopularProductStrategyTest {

    private static final Product 키보드_리뷰_개수_두_개_별점_한_개 = Product.builder()
            .id(1L)
            .name("키보드")
            .imageUrl("이미지")
            .category(Category.KEYBOARD)
            .reviewCount(2)
            .rating(1)
            .build();
    private static final Product 마우스_리뷰_개수_한_개_별점_두_개 = Product.builder()
            .id(2L)
            .name("마우스")
            .imageUrl("이미지")
            .category(Category.MOUSE)
            .reviewCount(1)
            .rating(2)
            .build();

    public static Stream<Arguments> definitionProvider() {
        PopularProductCriteriaDefinition 리뷰_개수_한_개_별점_한_개_이상 = new PopularProductCriteriaDefinition() {
            @Override
            public int getReviewCount() {
                return 1;
            }

            @Override
            public double getRating() {
                return 1;
            }

            @Override
            public int getPopularProductsSize() {
                return 2;
            }
        };

        PopularProductCriteriaDefinition 리뷰_개수_두_개_별점_한_개_이상 = new PopularProductCriteriaDefinition() {
            @Override
            public int getReviewCount() {
                return 2;
            }

            @Override
            public double getRating() {
                return 1;
            }

            @Override
            public int getPopularProductsSize() {
                return 2;
            }
        };

        PopularProductCriteriaDefinition 리뷰_개수_한_개_별점_두_개_이상 = new PopularProductCriteriaDefinition() {
            @Override
            public int getReviewCount() {
                return 1;
            }

            @Override
            public double getRating() {
                return 2;
            }

            @Override
            public int getPopularProductsSize() {
                return 2;
            }
        };

        PopularProductCriteriaDefinition 리뷰_개수_세_개_별점_세_개_이상 = new PopularProductCriteriaDefinition() {
            @Override
            public int getReviewCount() {
                return 3;
            }

            @Override
            public double getRating() {
                return 3;
            }

            @Override
            public int getPopularProductsSize() {
                return 2;
            }
        };
        return Stream.of(
                Arguments.of(리뷰_개수_한_개_별점_한_개_이상, PopularProductsResponse.from(List.of(키보드_리뷰_개수_두_개_별점_한_개,
                        마우스_리뷰_개수_한_개_별점_두_개))),
                Arguments.of(리뷰_개수_두_개_별점_한_개_이상, PopularProductsResponse.from(List.of(키보드_리뷰_개수_두_개_별점_한_개))),
                Arguments.of(리뷰_개수_한_개_별점_두_개_이상, PopularProductsResponse.from(List.of(마우스_리뷰_개수_한_개_별점_두_개))),
                Arguments.of(리뷰_개수_세_개_별점_세_개_이상, PopularProductsResponse.from(List.of()))
        );
    }

    @ParameterizedTest
    @MethodSource("definitionProvider")
    void 인기_제품_조회(PopularProductCriteriaDefinition definition, PopularProductsResponse expected) {
        // given
        PopularProductStrategy strategy = new ShufflingPopularProductStrategy(definition);

        // when
        PopularProductsResponse response = strategy.getResult(new FakeFindPopularProductCallback(키보드_리뷰_개수_두_개_별점_한_개,
                마우스_리뷰_개수_한_개_별점_두_개));

        // then
        assertThat(response.getItems()).usingRecursiveFieldByFieldElementComparator()
                .containsAll(expected.getItems());
    }
}
