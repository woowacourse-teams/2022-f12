package com.woowacourse.f12.acceptance;

import static com.woowacourse.f12.acceptance.support.LoginUtil.로그인을_한다;
import static com.woowacourse.f12.acceptance.support.RestAssuredRequestUtil.GET_요청을_보낸다;
import static com.woowacourse.f12.support.ProductFixture.KEYBOARD_1;
import static com.woowacourse.f12.support.ProductFixture.KEYBOARD_2;
import static com.woowacourse.f12.support.ProductFixture.MOUSE_1;
import static com.woowacourse.f12.support.ReviewFixtures.REVIEW_RATING_3;
import static com.woowacourse.f12.support.ReviewFixtures.REVIEW_RATING_4;
import static com.woowacourse.f12.support.ReviewFixtures.REVIEW_RATING_5;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.woowacourse.f12.domain.product.Product;
import com.woowacourse.f12.domain.product.ProductRepository;
import com.woowacourse.f12.dto.response.product.ProductPageResponse;
import com.woowacourse.f12.dto.response.product.ProductResponse;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

class ProductAcceptanceTest extends AcceptanceTest {

    @Autowired
    private ProductRepository productRepository;

    @Test
    void 단일_제품_조회한다() {
        // given
        Product product = 제품을_저장한다(KEYBOARD_1.생성());

        // when
        ExtractableResponse<Response> response = GET_요청을_보낸다("/api/v1/products/" + product.getId());

        // then
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(response.as(ProductResponse.class)).usingRecursiveComparison()
                        .isEqualTo(product)
        );
    }

    @Test
    void 모든_제품_목록을_페이징하여_조회한다() {
        // given
        Product product = 제품을_저장한다(KEYBOARD_1.생성());
        제품을_저장한다(MOUSE_1.생성());

        // when
        ExtractableResponse<Response> response = GET_요청을_보낸다("/api/v1/products?page=0&size=1");

        // then
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(response.as(ProductPageResponse.class).getItems())
                        .extracting("id")
                        .containsExactly(product.getId()),
                () -> assertThat(response.as(ProductPageResponse.class).isHasNext()).isTrue()
        );
    }

    @Test
    void 특정_카테고리_목록을_페이징하여_조회한다() {
        // given
        제품을_저장한다(KEYBOARD_1.생성());
        제품을_저장한다(KEYBOARD_2.생성());
        Product product = 제품을_저장한다(MOUSE_1.생성());

        // when
        ExtractableResponse<Response> response = GET_요청을_보낸다("/api/v1/products?category=MOUSE&page=0&size=1");

        // then
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(response.as(ProductPageResponse.class).getItems())
                        .extracting("id")
                        .containsExactly(product.getId()),
                () -> assertThat(response.as(ProductPageResponse.class).isHasNext()).isFalse()
        );
    }

    @Test
    void 키보드_목록을_리뷰가_많은_순서로_페이징하여_조회한다() {
        // given
        Product product1 = 제품을_저장한다(KEYBOARD_1.생성());
        Product product2 = 제품을_저장한다(KEYBOARD_2.생성());
        String token = 로그인을_한다("1").getToken();
        REVIEW_RATING_5.작성_요청을_보낸다(product1.getId(), token);
        REVIEW_RATING_4.작성_요청을_보낸다(product1.getId(), token);
        REVIEW_RATING_3.작성_요청을_보낸다(product2.getId(), token);

        // when
        ExtractableResponse<Response> response = GET_요청을_보낸다("/api/v1/products?page=0&size=1&sort=reviewCount,desc");

        // then
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(response.as(ProductPageResponse.class).getItems())
                        .extracting("id")
                        .containsExactly(product1.getId()),
                () -> assertThat(response.as(ProductPageResponse.class).isHasNext()).isTrue()
        );
    }

    @Test
    void 키보드_목록을_평점_높은_순서로_페이징하여_조회한다() {
        // given
        Product product1 = 제품을_저장한다(KEYBOARD_1.생성());
        Product product2 = 제품을_저장한다(KEYBOARD_2.생성());
        String token = 로그인을_한다("1").getToken();
        REVIEW_RATING_4.작성_요청을_보낸다(product1.getId(), token);
        REVIEW_RATING_5.작성_요청을_보낸다(product2.getId(), token);

        // when
        ExtractableResponse<Response> response = GET_요청을_보낸다("/api/v1/products?page=0&size=1&sort=rating,desc");

        // then
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(response.as(ProductPageResponse.class).getItems())
                        .extracting("id")
                        .containsExactly(product2.getId()),
                () -> assertThat(response.as(ProductPageResponse.class).isHasNext()).isTrue()
        );
    }

    private Product 제품을_저장한다(Product product) {
        return productRepository.save(product);
    }
}
