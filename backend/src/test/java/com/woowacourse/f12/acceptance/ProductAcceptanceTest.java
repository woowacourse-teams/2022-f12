package com.woowacourse.f12.acceptance;

import com.woowacourse.f12.domain.product.Product;
import com.woowacourse.f12.domain.product.ProductRepository;
import com.woowacourse.f12.dto.request.member.MemberRequest;
import com.woowacourse.f12.dto.response.auth.LoginResponse;
import com.woowacourse.f12.dto.response.product.ProductPageResponse;
import com.woowacourse.f12.dto.response.product.ProductResponse;
import com.woowacourse.f12.dto.response.product.ProductStatisticsResponse;
import com.woowacourse.f12.presentation.product.CategoryConstant;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import java.util.Map;

import static com.woowacourse.f12.acceptance.support.LoginUtil.로그인을_한다;
import static com.woowacourse.f12.acceptance.support.RestAssuredRequestUtil.GET_요청을_보낸다;
import static com.woowacourse.f12.acceptance.support.RestAssuredRequestUtil.로그인된_상태로_PATCH_요청을_보낸다;
import static com.woowacourse.f12.presentation.member.CareerLevelConstant.*;
import static com.woowacourse.f12.presentation.member.JobTypeConstant.*;
import static com.woowacourse.f12.presentation.product.CategoryConstant.KEYBOARD_CONSTANT;
import static com.woowacourse.f12.support.GitHubProfileFixtures.CORINNE_GITHUB;
import static com.woowacourse.f12.support.GitHubProfileFixtures.MINCHO_GITHUB;
import static com.woowacourse.f12.support.ProductFixture.*;
import static com.woowacourse.f12.support.ReviewFixtures.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

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
        ProductResponse productResponse = response.as(ProductResponse.class);
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(productResponse.getCategory()).isEqualTo(KEYBOARD_CONSTANT),
                () -> assertThat(productResponse).usingRecursiveComparison()
                        .ignoringFieldsOfTypes(CategoryConstant.class)
                        .isEqualTo(product)
        );
    }

    @Test
    void 모든_제품_목록을_페이징하여_조회한다() {
        // given
        제품을_저장한다(KEYBOARD_1.생성());
        Product product = 제품을_저장한다(MOUSE_1.생성());

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
        Product keyboard1 = 제품을_저장한다(KEYBOARD_1.생성());
        Product keyboard2 = 제품을_저장한다(KEYBOARD_2.생성());
        제품을_저장한다(MOUSE_1.생성());

        // when
        ExtractableResponse<Response> response = GET_요청을_보낸다("/api/v1/products?category=keyboard&page=0&size=2");

        // then
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(response.as(ProductPageResponse.class).getItems())
                        .extracting("id")
                        .containsExactly(keyboard2.getId(), keyboard1.getId()),
                () -> assertThat(response.as(ProductPageResponse.class).isHasNext()).isFalse()
        );
    }

    @Test
    void 키보드_목록을_리뷰가_많은_순서로_페이징하여_조회한다() {
        // given
        Product product1 = 제품을_저장한다(KEYBOARD_1.생성());
        Product product2 = 제품을_저장한다(KEYBOARD_2.생성());
        MemberRequest memberRequest = new MemberRequest(SENIOR_CONSTANT, BACKEND_CONSTANT);

        String corinneToken = 로그인을_한다(CORINNE_GITHUB.getCode()).getToken();
        로그인된_상태로_PATCH_요청을_보낸다("/api/v1/members/me", corinneToken, memberRequest);
        String minchoToken = 로그인을_한다(MINCHO_GITHUB.getCode()).getToken();
        로그인된_상태로_PATCH_요청을_보낸다("/api/v1/members/me", minchoToken, memberRequest);

        REVIEW_RATING_5.작성_요청을_보낸다(product1.getId(), corinneToken);
        REVIEW_RATING_4.작성_요청을_보낸다(product1.getId(), minchoToken);
        REVIEW_RATING_3.작성_요청을_보낸다(product2.getId(), minchoToken);

        // when
        ExtractableResponse<Response> response = GET_요청을_보낸다(
                "/api/v1/products?category=keyboard&page=0&size=2&sort=reviewCount,desc");

        // then
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(response.as(ProductPageResponse.class).getItems())
                        .extracting("id")
                        .containsExactly(product1.getId(), product2.getId()),
                () -> assertThat(response.as(ProductPageResponse.class).isHasNext()).isFalse()
        );
    }

    @Test
    void 리뷰_개수가_같은_상태에서_제품_목록을_리뷰가_많은_순서로_페이징하여_조회하면_id_역순으로_조회된다() {
        // given
        Product product1 = 제품을_저장한다(KEYBOARD_1.생성());
        Product product2 = 제품을_저장한다(KEYBOARD_2.생성());
        String token = 로그인을_한다(CORINNE_GITHUB.getCode()).getToken();
        REVIEW_RATING_5.작성_요청을_보낸다(product1.getId(), token);
        REVIEW_RATING_3.작성_요청을_보낸다(product2.getId(), token);

        // when
        ExtractableResponse<Response> response = GET_요청을_보낸다(
                "/api/v1/products?category=keyboard&page=0&size=2&sort=reviewCount,desc");

        // then
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(response.as(ProductPageResponse.class).getItems())
                        .extracting("id")
                        .containsExactly(product2.getId(), product1.getId()),
                () -> assertThat(response.as(ProductPageResponse.class).isHasNext()).isFalse()
        );
    }

    @Test
    void 정렬_조건으로_id_역순으로_페이징하여_조회하면_id_역순으로_조회된다() {
        // given
        Product product1 = 제품을_저장한다(KEYBOARD_1.생성());
        Product product2 = 제품을_저장한다(KEYBOARD_2.생성());
        Product product3 = 제품을_저장한다(MOUSE_1.생성());

        // when
        ExtractableResponse<Response> response = GET_요청을_보낸다(
                "/api/v1/products?page=0&size=3&sort=id,desc");

        // then
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(response.as(ProductPageResponse.class).getItems())
                        .extracting("id")
                        .containsExactly(product3.getId(), product2.getId(), product1.getId()),
                () -> assertThat(response.as(ProductPageResponse.class).isHasNext()).isFalse()
        );
    }

    @Test
    void 키보드_목록을_평점_높은_순서로_페이징하여_조회한다() {
        // given
        Product product1 = 제품을_저장한다(KEYBOARD_1.생성());
        Product product2 = 제품을_저장한다(KEYBOARD_2.생성());
        String token = 로그인을_한다(CORINNE_GITHUB.getCode()).getToken();
        REVIEW_RATING_4.작성_요청을_보낸다(product1.getId(), token);
        REVIEW_RATING_5.작성_요청을_보낸다(product2.getId(), token);

        // when
        ExtractableResponse<Response> response = GET_요청을_보낸다(
                "/api/v1/products?category=keyboard&page=0&size=1&sort=rating,desc");

        // then
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(response.as(ProductPageResponse.class).getItems())
                        .extracting("id")
                        .containsExactly(product2.getId()),
                () -> assertThat(response.as(ProductPageResponse.class).isHasNext()).isTrue()
        );
    }

    @Test
    void 제품의_사용자_통계를_조회한다() {
        // given
        Product product = 제품을_저장한다(KEYBOARD_1.생성());

        LoginResponse firstLoginResponse = 로그인을_한다(MINCHO_GITHUB.getCode());
        String firstToken = firstLoginResponse.getToken();
        MemberRequest firstMemberRequest = new MemberRequest(SENIOR_CONSTANT, BACKEND_CONSTANT);
        로그인된_상태로_PATCH_요청을_보낸다("/api/v1/members/me", firstToken, firstMemberRequest);
        REVIEW_RATING_5.작성_요청을_보낸다(product.getId(), firstToken);

        LoginResponse secondLoginResponse = 로그인을_한다(CORINNE_GITHUB.getCode());
        String secondToken = secondLoginResponse.getToken();
        MemberRequest secondMemberRequest = new MemberRequest(JUNIOR_CONSTANT, FRONTEND_CONSTANT);
        로그인된_상태로_PATCH_요청을_보낸다("/api/v1/members/me", secondToken, secondMemberRequest);
        REVIEW_RATING_5.작성_요청을_보낸다(product.getId(), secondToken);

        // when
        ExtractableResponse<Response> response = GET_요청을_보낸다("/api/v1/products/" + product.getId() + "/statistics");
        ProductStatisticsResponse productStatisticsResponse = response.as(ProductStatisticsResponse.class);

        // then
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(productStatisticsResponse.getCareerLevel()).usingRecursiveComparison()
                        .isEqualTo(Map.of(NONE_CONSTANT, 0.0, JUNIOR_CONSTANT, 0.5, MID_LEVEL_CONSTANT, 0.0,
                                SENIOR_CONSTANT, 0.5)),
                () -> assertThat(productStatisticsResponse.getJobType()).usingRecursiveComparison()
                        .isEqualTo(Map.of(FRONTEND_CONSTANT, 0.5, BACKEND_CONSTANT, 0.5, MOBILE_CONSTANT, 0.0,
                                ETC_CONSTANT, 0.0))
        );
    }

    @Test
    void 제품명으로_제품_목록을_검색한다() {
        // given
        Product keyboard1 = 제품을_저장한다(KEYBOARD_1.생성());
        제품을_저장한다(KEYBOARD_2.생성());
        Product mouse1 = 제품을_저장한다(MOUSE_1.생성());

        // when
        ExtractableResponse<Response> response = GET_요청을_보낸다("/api/v1/products?query=1");
        ProductPageResponse productPageResponse = response.as(ProductPageResponse.class);

        // then
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(productPageResponse.isHasNext()).isFalse(),
                () -> assertThat(productPageResponse.getItems()).usingRecursiveFieldByFieldElementComparator()
                        .containsExactly(ProductResponse.from(mouse1), ProductResponse.from(keyboard1))
        );
    }

    @Test
    void 제품명과_카테고리로_제품_목록을_검색한다() {
        // given
        Product keyboard1 = 제품을_저장한다(KEYBOARD_1.생성());
        제품을_저장한다(KEYBOARD_2.생성());
        제품을_저장한다(MOUSE_1.생성());

        // when
        ExtractableResponse<Response> response = GET_요청을_보낸다("/api/v1/products?query=1&category=keyboard");
        ProductPageResponse productPageResponse = response.as(ProductPageResponse.class);

        // then
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(productPageResponse.isHasNext()).isFalse(),
                () -> assertThat(productPageResponse.getItems()).usingRecursiveFieldByFieldElementComparator()
                        .containsExactly(ProductResponse.from(keyboard1))
        );
    }

    private Product 제품을_저장한다(Product product) {
        return productRepository.save(product);
    }
}
