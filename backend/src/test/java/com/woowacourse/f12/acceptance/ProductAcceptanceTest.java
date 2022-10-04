package com.woowacourse.f12.acceptance;

import static com.woowacourse.f12.acceptance.support.RestAssuredRequestUtil.GET_요청을_보낸다;
import static com.woowacourse.f12.acceptance.support.RestAssuredRequestUtil.로그인된_상태로_POST_요청을_보낸다;
import static com.woowacourse.f12.acceptance.support.RestAssuredRequestUtil.로그인된_상태로_PUT_요청을_보낸다;
import static com.woowacourse.f12.presentation.member.CareerLevelConstant.JUNIOR_CONSTANT;
import static com.woowacourse.f12.presentation.member.CareerLevelConstant.MID_LEVEL_CONSTANT;
import static com.woowacourse.f12.presentation.member.CareerLevelConstant.NONE_CONSTANT;
import static com.woowacourse.f12.presentation.member.CareerLevelConstant.SENIOR_CONSTANT;
import static com.woowacourse.f12.presentation.member.JobTypeConstant.BACKEND_CONSTANT;
import static com.woowacourse.f12.presentation.member.JobTypeConstant.ETC_CONSTANT;
import static com.woowacourse.f12.presentation.member.JobTypeConstant.FRONTEND_CONSTANT;
import static com.woowacourse.f12.presentation.member.JobTypeConstant.MOBILE_CONSTANT;
import static com.woowacourse.f12.presentation.product.CategoryConstant.KEYBOARD_CONSTANT;
import static com.woowacourse.f12.support.fixture.AcceptanceFixture.민초;
import static com.woowacourse.f12.support.fixture.AcceptanceFixture.코린;
import static com.woowacourse.f12.support.fixture.AcceptanceFixture.클레이;
import static com.woowacourse.f12.support.fixture.MemberFixture.ADMIN_KLAY;
import static com.woowacourse.f12.support.fixture.ProductFixture.KEYBOARD_1;
import static com.woowacourse.f12.support.fixture.ProductFixture.KEYBOARD_2;
import static com.woowacourse.f12.support.fixture.ProductFixture.MOUSE_1;
import static com.woowacourse.f12.support.fixture.ReviewFixture.REVIEW_RATING_3;
import static com.woowacourse.f12.support.fixture.ReviewFixture.REVIEW_RATING_4;
import static com.woowacourse.f12.support.fixture.ReviewFixture.REVIEW_RATING_5;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.woowacourse.f12.domain.member.Member;
import com.woowacourse.f12.domain.member.MemberRepository;
import com.woowacourse.f12.domain.product.Category;
import com.woowacourse.f12.domain.product.Product;
import com.woowacourse.f12.domain.product.ProductRepository;
import com.woowacourse.f12.dto.request.member.MemberRequest;
import com.woowacourse.f12.dto.request.product.ProductCreateRequest;
import com.woowacourse.f12.dto.request.product.ProductUpdateRequest;
import com.woowacourse.f12.dto.response.auth.LoginResponse;
import com.woowacourse.f12.dto.response.product.ProductPageResponse;
import com.woowacourse.f12.dto.response.product.ProductResponse;
import com.woowacourse.f12.dto.response.product.ProductStatisticsResponse;
import com.woowacourse.f12.presentation.product.CategoryConstant;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;

class ProductAcceptanceTest extends AcceptanceTest {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private MemberRepository memberRepository;

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
                        .ignoringFields("rating")
                        .isEqualTo(product)
        );
    }

    @Test
    void 모든_제품_목록을_키워드와_옵션없이_페이징하여_조회한다() {
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

        String corinneToken = 코린.로그인을_한다().getToken();
        코린.로그인한_상태로(corinneToken).추가정보를_입력한다(memberRequest);
        String minchoToken = 민초.로그인을_한다().getToken();
        민초.로그인한_상태로(minchoToken).추가정보를_입력한다(memberRequest);

        코린.로그인한_상태로(corinneToken).리뷰를_작성한다(product1.getId(), REVIEW_RATING_5);
        민초.로그인한_상태로(minchoToken).리뷰를_작성한다(product1.getId(), REVIEW_RATING_4);
        민초.로그인한_상태로(minchoToken).리뷰를_작성한다(product2.getId(), REVIEW_RATING_3);

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
    void 리뷰_개수가_같은_상태에서_제품_목록을_리뷰가_많은_순서로_페이징하여_조회하면_평점_높은순_id_역순으로_조회된다() {
        // given
        Product product1 = 제품을_저장한다(KEYBOARD_1.생성());
        Product product2 = 제품을_저장한다(KEYBOARD_2.생성());
        Product product3 = 제품을_저장한다(MOUSE_1.생성());
        MemberRequest memberRequest = new MemberRequest(SENIOR_CONSTANT, BACKEND_CONSTANT);
        String token = 코린.로그인을_한다().getToken();
        코린.로그인한_상태로(token).추가정보를_입력한다(memberRequest);

        코린.로그인한_상태로(token).리뷰를_작성한다(product1.getId(), REVIEW_RATING_5);
        코린.로그인한_상태로(token).리뷰를_작성한다(product2.getId(), REVIEW_RATING_3);
        코린.로그인한_상태로(token).리뷰를_작성한다(product3.getId(), REVIEW_RATING_5);

        // when
        ExtractableResponse<Response> response = GET_요청을_보낸다(
                "/api/v1/products?page=0&size=3&sort=reviewCount,desc");

        // then
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(response.as(ProductPageResponse.class).getItems())
                        .extracting("id")
                        .containsExactly(product3.getId(), product1.getId(), product2.getId()),
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
        MemberRequest memberRequest = new MemberRequest(SENIOR_CONSTANT, BACKEND_CONSTANT);
        String token = 코린.로그인을_한다().getToken();
        코린.로그인한_상태로(token).추가정보를_입력한다(memberRequest);

        코린.로그인한_상태로(token).리뷰를_작성한다(product1.getId(), REVIEW_RATING_4);
        코린.로그인한_상태로(token).리뷰를_작성한다(product2.getId(), REVIEW_RATING_5);

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

        MemberRequest corinneMemberRequest = new MemberRequest(SENIOR_CONSTANT, BACKEND_CONSTANT);
        String corinneToken = 코린.로그인을_한다().getToken();
        코린.로그인한_상태로(corinneToken).추가정보를_입력한다(corinneMemberRequest);
        코린.로그인한_상태로(corinneToken).리뷰를_작성한다(product.getId(), REVIEW_RATING_5);

        MemberRequest minchoMemberRequest = new MemberRequest(JUNIOR_CONSTANT, FRONTEND_CONSTANT);
        String minchoToken = 민초.로그인을_한다().getToken();
        민초.로그인한_상태로(minchoToken).추가정보를_입력한다(minchoMemberRequest);
        민초.로그인한_상태로(minchoToken).리뷰를_작성한다(product.getId(), REVIEW_RATING_5);

        // when
        ExtractableResponse<Response> response = GET_요청을_보낸다("/api/v1/products/" + product.getId() + "/statistics");

        // then
        ProductStatisticsResponse productStatisticsResponse = response.as(ProductStatisticsResponse.class);

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

        // then
        ProductPageResponse productPageResponse = response.as(ProductPageResponse.class);

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

    @Test
    void 어드민으로_로그인_하여_제품을_추가한다() {
        // given
        어드민을_저장한다(ADMIN_KLAY.생성());
        final LoginResponse loginResponse = 클레이.로그인을_한다();
        String loginToken = loginResponse.getToken();

        // when
        final ProductCreateRequest productCreateRequest = new ProductCreateRequest("keyboard", "keyboard.url",
                Category.KEYBOARD);
        final ExtractableResponse<Response> response = 로그인된_상태로_POST_요청을_보낸다("/api/v1/products", loginToken,
                productCreateRequest);

        // then
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value()),
                () -> assertThat(response.header(HttpHeaders.LOCATION)).startsWith("/api/v1/products/")
        );
    }

    @Test
    void 어드민으로_로그인_하여_제품을_수정한다() {
        // given
        final Product savedProduct = 제품을_저장한다(KEYBOARD_1.생성());

        어드민을_저장한다(ADMIN_KLAY.생성());
        final LoginResponse loginResponse = 클레이.로그인을_한다();
        String loginToken = loginResponse.getToken();

        // when
        final ProductUpdateRequest productUpdateRequest = new ProductUpdateRequest("updatedName", "updatedURL",
                Category.MONITOR);
        final ExtractableResponse<Response> response = 로그인된_상태로_PUT_요청을_보낸다("/api/v1/products/" + savedProduct.getId(),
                loginToken, productUpdateRequest);

        // then
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value())
        );
    }

    private Member 어드민을_저장한다(Member member) {
        return memberRepository.save(member);
    }

    private Product 제품을_저장한다(Product product) {
        return productRepository.save(product);
    }
}
