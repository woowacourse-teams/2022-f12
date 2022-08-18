package com.woowacourse.f12.acceptance;

import static com.woowacourse.f12.acceptance.support.LoginUtil.로그인을_한다;
import static com.woowacourse.f12.acceptance.support.RestAssuredRequestUtil.GET_요청을_보낸다;
import static com.woowacourse.f12.acceptance.support.RestAssuredRequestUtil.로그인된_상태로_DELETE_요청을_보낸다;
import static com.woowacourse.f12.acceptance.support.RestAssuredRequestUtil.로그인된_상태로_GET_요청을_보낸다;
import static com.woowacourse.f12.acceptance.support.RestAssuredRequestUtil.로그인된_상태로_PATCH_요청을_보낸다;
import static com.woowacourse.f12.acceptance.support.RestAssuredRequestUtil.로그인된_상태로_PUT_요청을_보낸다;
import static com.woowacourse.f12.presentation.member.CareerLevelConstant.SENIOR_CONSTANT;
import static com.woowacourse.f12.presentation.member.JobTypeConstant.BACKEND_CONSTANT;
import static com.woowacourse.f12.support.fixture.GitHubProfileFixture.CORINNE_GITHUB;
import static com.woowacourse.f12.support.fixture.GitHubProfileFixture.MINCHO_GITHUB;
import static com.woowacourse.f12.support.fixture.MemberFixture.MINCHO;
import static com.woowacourse.f12.support.fixture.ProductFixture.KEYBOARD_1;
import static com.woowacourse.f12.support.fixture.ProductFixture.KEYBOARD_2;
import static com.woowacourse.f12.support.fixture.ReviewFixture.REVIEW_RATING_4;
import static com.woowacourse.f12.support.fixture.ReviewFixture.REVIEW_RATING_5;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.woowacourse.f12.domain.product.Product;
import com.woowacourse.f12.domain.product.ProductRepository;
import com.woowacourse.f12.dto.request.member.MemberRequest;
import com.woowacourse.f12.dto.request.review.ReviewRequest;
import com.woowacourse.f12.dto.response.ExceptionResponse;
import com.woowacourse.f12.dto.response.auth.LoginResponse;
import com.woowacourse.f12.dto.response.inventoryproduct.InventoryProductResponse;
import com.woowacourse.f12.dto.response.inventoryproduct.InventoryProductsResponse;
import com.woowacourse.f12.dto.response.product.ProductResponse;
import com.woowacourse.f12.dto.response.review.ReviewWithAuthorAndProductPageResponse;
import com.woowacourse.f12.dto.response.review.ReviewWithAuthorAndProductResponse;
import com.woowacourse.f12.dto.response.review.ReviewWithAuthorPageResponse;
import com.woowacourse.f12.dto.response.review.ReviewWithAuthorResponse;
import com.woowacourse.f12.dto.response.review.ReviewWithProductPageResponse;
import com.woowacourse.f12.dto.response.review.ReviewWithProductResponse;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

class ReviewAcceptanceTest extends AcceptanceTest {

    @Autowired
    private ProductRepository productRepository;

    @Test
    void 키보드가_저장되어있고_키보드에_대한_리뷰를_작성한다() {
        // given
        Product product = 제품을_저장한다(KEYBOARD_1.생성());
        String token = 로그인을_한다(CORINNE_GITHUB.getCode()).getToken();
        MemberRequest memberRequest = new MemberRequest(SENIOR_CONSTANT, BACKEND_CONSTANT);
        로그인된_상태로_PATCH_요청을_보낸다("/api/v1/members/me", token, memberRequest);

        // when
        ExtractableResponse<Response> response = REVIEW_RATING_5.작성_요청을_보낸다(product.getId(), token);

        // then
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value()),
                () -> assertThat(response.header("Location")).contains("/api/v1/reviews/")
        );
    }

    @Test
    void 같은_회원이_같은_제품에_리뷰를_중복해서_작성하면_예외가_발생한다() {
        // given
        Product product = 제품을_저장한다(KEYBOARD_1.생성());
        String token = 로그인을_한다(CORINNE_GITHUB.getCode()).getToken();
        MemberRequest memberRequest = new MemberRequest(SENIOR_CONSTANT, BACKEND_CONSTANT);
        로그인된_상태로_PATCH_요청을_보낸다("/api/v1/members/me", token, memberRequest);
        REVIEW_RATING_5.작성_요청을_보낸다(product.getId(), token);

        // when
        ExtractableResponse<Response> response = REVIEW_RATING_5.작성_요청을_보낸다(product.getId(), token);

        // then
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value()),
                () -> assertThat(response.as(ExceptionResponse.class).getMessage())
                        .isEqualTo("해당 제품에 대해 이미 리뷰가 작성되어 있습니다.")
        );
    }

    @Test
    void 로그인_안한_상태로_특정_제품_리뷰_목록을_최신순으로_조회한다() {
        // given
        Product product = 제품을_저장한다(KEYBOARD_1.생성());
        MemberRequest memberRequest = new MemberRequest(SENIOR_CONSTANT, BACKEND_CONSTANT);

        String token = 로그인을_한다(CORINNE_GITHUB.getCode()).getToken();
        로그인된_상태로_PATCH_요청을_보낸다("/api/v1/members/me", token, memberRequest);
        REVIEW_RATING_5.작성_요청을_보낸다(product.getId(), token);

        String token2 = 로그인을_한다(MINCHO_GITHUB.getCode()).getToken();
        로그인된_상태로_PATCH_요청을_보낸다("/api/v1/members/me", token2, memberRequest);
        Long reviewId = Location_헤더에서_id값을_꺼낸다(REVIEW_RATING_5.작성_요청을_보낸다(product.getId(), token2));

        // when
        String url = "/api/v1/products/" + product.getId() + "/reviews?size=1&page=0&sort=createdAt,desc";
        ExtractableResponse<Response> response = GET_요청을_보낸다(url);

        // then
        ReviewWithAuthorPageResponse reviewPageResponse = response.as(ReviewWithAuthorPageResponse.class);
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(reviewPageResponse.getItems())
                        .extracting("id")
                        .containsExactly(reviewId),
                () -> assertThat(reviewPageResponse.getItems())
                        .extracting("authorMatch")
                        .containsExactly(false),
                () -> assertThat(reviewPageResponse.isHasNext()).isTrue()
        );
    }

    @Test
    void 로그인_안한_상태로_특정_제품_리뷰_목록을_평점순으로_조회한다() {
        // given
        Product product = 제품을_저장한다(KEYBOARD_1.생성());
        String token = 로그인을_한다(CORINNE_GITHUB.getCode()).getToken();
        MemberRequest memberRequest = new MemberRequest(SENIOR_CONSTANT, BACKEND_CONSTANT);
        로그인된_상태로_PATCH_요청을_보낸다("/api/v1/members/me", token, memberRequest);
        REVIEW_RATING_4.작성_요청을_보낸다(product.getId(), token);
        String token2 = 로그인을_한다(MINCHO_GITHUB.getCode()).getToken();
        로그인된_상태로_PATCH_요청을_보낸다("/api/v1/members/me", token2, memberRequest);
        Long reviewId = Location_헤더에서_id값을_꺼낸다(REVIEW_RATING_5.작성_요청을_보낸다(product.getId(), token2));

        // when
        String url = "/api/v1/products/" + product.getId() + "/reviews?size=1&page=0&sort=rating,desc";
        ExtractableResponse<Response> response = GET_요청을_보낸다(url);

        // then
        ReviewWithAuthorPageResponse reviewPageResponse = response.as(ReviewWithAuthorPageResponse.class);
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(reviewPageResponse.getItems())
                        .extracting("id")
                        .containsExactly(reviewId),
                () -> assertThat(reviewPageResponse.getItems())
                        .extracting("authorMatch")
                        .containsExactly(false),
                () -> assertThat(reviewPageResponse.isHasNext()).isTrue()
        );
    }

    @Test
    void 내가_작성한_리뷰가_포함된_리뷰목록을_조회한다() {
        // given
        Product product = 제품을_저장한다(KEYBOARD_1.생성());

        String corinneToken = 로그인을_한다(CORINNE_GITHUB.getCode()).getToken();
        MemberRequest memberRequest = new MemberRequest(SENIOR_CONSTANT, BACKEND_CONSTANT);
        로그인된_상태로_PATCH_요청을_보낸다("/api/v1/members/me", corinneToken, memberRequest);
        Long corinneReviewId = Location_헤더에서_id값을_꺼낸다(REVIEW_RATING_4.작성_요청을_보낸다(product.getId(), corinneToken));

        String minchoToken = 로그인을_한다(MINCHO_GITHUB.getCode()).getToken();
        로그인된_상태로_PATCH_요청을_보낸다("/api/v1/members/me", minchoToken, memberRequest);
        Long minchoReviewId = Location_헤더에서_id값을_꺼낸다(REVIEW_RATING_5.작성_요청을_보낸다(product.getId(), minchoToken));

        // when
        String url = "/api/v1/products/" + product.getId() + "/reviews?size=2&page=0&sort=rating,desc";
        ExtractableResponse<Response> response = 로그인된_상태로_GET_요청을_보낸다(url, corinneToken);

        // then
        ReviewWithAuthorPageResponse reviewPageResponse = response.as(ReviewWithAuthorPageResponse.class);
        ReviewWithAuthorResponse corinneResponse = 리뷰_아이디에_해당하는_리뷰와_회원정보를_찾는다(reviewPageResponse,
                corinneReviewId);
        ReviewWithAuthorResponse minchoResponse = 리뷰_아이디에_해당하는_리뷰와_회원정보를_찾는다(reviewPageResponse, minchoReviewId);

        assertAll(
                () -> assertThat(corinneResponse.isAuthorMatch()).isTrue(),
                () -> assertThat(minchoResponse.isAuthorMatch()).isFalse()
        );
    }

    @Test
    void 전체_리뷰_목록을_최신순으로_조회한다() {
        // given
        Product product1 = 제품을_저장한다(KEYBOARD_1.생성());
        Product product2 = 제품을_저장한다(KEYBOARD_2.생성());
        String token = 로그인을_한다(CORINNE_GITHUB.getCode()).getToken();
        MemberRequest memberRequest = new MemberRequest(SENIOR_CONSTANT, BACKEND_CONSTANT);
        로그인된_상태로_PATCH_요청을_보낸다("/api/v1/members/me", token, memberRequest);
        Long reviewId1 = Location_헤더에서_id값을_꺼낸다(REVIEW_RATING_4.작성_요청을_보낸다(product1.getId(), token));
        Long reviewId2 = Location_헤더에서_id값을_꺼낸다(REVIEW_RATING_4.작성_요청을_보낸다(product2.getId(), token));

        // when
        ExtractableResponse<Response> response = GET_요청을_보낸다(
                "/api/v1/reviews?page=0&size=2&sort=createdAt,desc");

        // then
        ReviewWithAuthorAndProductPageResponse reviewWithAuthorAndProductPageResponse = response.as(
                ReviewWithAuthorAndProductPageResponse.class);
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(reviewWithAuthorAndProductPageResponse.isHasNext()).isFalse(),
                () -> assertThat(reviewWithAuthorAndProductPageResponse.getItems())
                        .extracting("id")
                        .containsExactly(reviewId2, reviewId1)
        );
    }

    @Test
    void 로그인한_회원이_리뷰_작성자와_일치하면_리뷰를_수정한다() {
        // given
        Product product = 제품을_저장한다(KEYBOARD_1.생성());
        String token = 로그인을_한다(CORINNE_GITHUB.getCode()).getToken();
        MemberRequest memberRequest = new MemberRequest(SENIOR_CONSTANT, BACKEND_CONSTANT);
        로그인된_상태로_PATCH_요청을_보낸다("/api/v1/members/me", token, memberRequest);
        Long reviewId = Location_헤더에서_id값을_꺼낸다(REVIEW_RATING_5.작성_요청을_보낸다(product.getId(), token));
        ReviewRequest requestBody = new ReviewRequest("수정된 내용", 4);

        // when
        ExtractableResponse<Response> response = 로그인된_상태로_PUT_요청을_보낸다("/api/v1/reviews/" + reviewId, token,
                requestBody);
        ReviewWithAuthorAndProductResponse review = GET_요청을_보낸다("/api/v1/reviews?page=0&size=2&sort=createdAt,desc")
                .as(ReviewWithAuthorAndProductPageResponse.class)
                .getItems()
                .get(0);

        // then
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value()),
                () -> assertThat(review.getContent()).isEqualTo(requestBody.getContent()),
                () -> assertThat(review.getRating()).isEqualTo(requestBody.getRating())
        );
    }

    @Test
    void 로그인한_회원이_리뷰_작성자와_일치하면_리뷰를_삭제하고_인벤토리_장비도_삭제한다() {
        // given
        Product product = 제품을_저장한다(KEYBOARD_1.생성());
        String token = 로그인을_한다(CORINNE_GITHUB.getCode()).getToken();
        MemberRequest memberRequest = new MemberRequest(SENIOR_CONSTANT, BACKEND_CONSTANT);
        로그인된_상태로_PATCH_요청을_보낸다("/api/v1/members/me", token, memberRequest);
        Long reviewId = Location_헤더에서_id값을_꺼낸다(REVIEW_RATING_5.작성_요청을_보낸다(product.getId(), token));

        // when
        ExtractableResponse<Response> response = 로그인된_상태로_DELETE_요청을_보낸다(
                "/api/v1/reviews/" + reviewId, token);
        List<ReviewWithAuthorAndProductResponse> reviews = GET_요청을_보낸다(
                "/api/v1/reviews?page=0&size=2&sort=createdAt,desc")
                .as(ReviewWithAuthorAndProductPageResponse.class)
                .getItems();
        List<InventoryProductResponse> inventoryProducts = 로그인된_상태로_GET_요청을_보낸다("/api/v1/members/inventoryProducts",
                token)
                .as(InventoryProductsResponse.class)
                .getItems();

        // then
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value()),
                () -> assertThat(reviews).isEmpty(),
                () -> assertThat(inventoryProducts).isEmpty()
        );
    }

    @Test
    void 다른_회원의_아이디로_리뷰_목록을_최신순으로_조회한다() {
        // given
        Product product1 = 제품을_저장한다(KEYBOARD_1.생성());
        Product product2 = 제품을_저장한다(KEYBOARD_2.생성());
        LoginResponse loginResponse = 로그인을_한다(CORINNE_GITHUB.getCode());
        String token = loginResponse.getToken();
        MemberRequest memberRequest = new MemberRequest(SENIOR_CONSTANT, BACKEND_CONSTANT);
        로그인된_상태로_PATCH_요청을_보낸다("/api/v1/members/me", token, memberRequest);
        Long reviewId1 = Location_헤더에서_id값을_꺼낸다(REVIEW_RATING_4.작성_요청을_보낸다(product1.getId(), token));
        Long reviewId2 = Location_헤더에서_id값을_꺼낸다(REVIEW_RATING_4.작성_요청을_보낸다(product2.getId(), token));

        // when
        ExtractableResponse<Response> response = GET_요청을_보낸다(
                "/api/v1/members/" + loginResponse.getMember().getId() + "/reviews?page=0&size=2&sort=createdAt,desc");

        // then
        ReviewWithProductPageResponse reviewWithProductPageResponse = response.as(ReviewWithProductPageResponse.class);
        List<ReviewWithProductResponse> reviewWithProductResponses = reviewWithProductPageResponse.getItems();
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(reviewWithProductPageResponse.isHasNext()).isFalse(),
                () -> assertThat(reviewWithProductResponses)
                        .extracting("id")
                        .containsExactly(reviewId2, reviewId1),
                () -> assertThat(reviewWithProductResponses.stream()
                        .map(it -> it.getProduct().getId())
                        .collect(Collectors.toList()))
                        .containsExactly(product2.getId(), product1.getId())
        );
    }

    @Test
    void 내_리뷰_목록을_최신순으로_조회한다() {
        // given
        Product product1 = 제품을_저장한다(KEYBOARD_1.생성());
        Product product2 = 제품을_저장한다(KEYBOARD_2.생성());
        LoginResponse loginResponse = 로그인을_한다(CORINNE_GITHUB.getCode());
        String token = loginResponse.getToken();
        MemberRequest memberRequest = new MemberRequest(SENIOR_CONSTANT, BACKEND_CONSTANT);
        로그인된_상태로_PATCH_요청을_보낸다("/api/v1/members/me", token, memberRequest);
        Long reviewId1 = Location_헤더에서_id값을_꺼낸다(REVIEW_RATING_4.작성_요청을_보낸다(product1.getId(), token));
        Long reviewId2 = Location_헤더에서_id값을_꺼낸다(REVIEW_RATING_4.작성_요청을_보낸다(product2.getId(), token));

        // when
        ExtractableResponse<Response> response = 로그인된_상태로_GET_요청을_보낸다(
                "/api/v1/members/me/reviews?page=0&size=2&sort=createdAt,desc", token);

        // then
        ReviewWithProductPageResponse reviewWithProductPageResponse = response.as(ReviewWithProductPageResponse.class);
        List<ReviewWithProductResponse> reviewWithProductResponses = reviewWithProductPageResponse.getItems();
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(reviewWithProductPageResponse.isHasNext()).isFalse(),
                () -> assertThat(reviewWithProductResponses)
                        .extracting("id")
                        .containsExactly(reviewId2, reviewId1),
                () -> assertThat(reviewWithProductResponses.stream()
                        .map(it -> it.getProduct().getId())
                        .collect(Collectors.toList()))
                        .containsExactly(product2.getId(), product1.getId())
        );
    }

    @Test
    void 인벤토리_아이디로_리뷰를_조회한다() {
        // given
        MemberRequest memberRequest = new MemberRequest(SENIOR_CONSTANT, BACKEND_CONSTANT);
        Product keyboard = 제품을_저장한다(KEYBOARD_1.생성());

        LoginResponse firstLoginResponse = 로그인을_한다(MINCHO_GITHUB.getCode());
        String token = firstLoginResponse.getToken();
        로그인된_상태로_PATCH_요청을_보낸다("/api/v1/members/me", token, memberRequest);

        ExtractableResponse<Response> reviewSaveResponse = REVIEW_RATING_5.작성_요청을_보낸다(keyboard.getId(), token);
        Long reviewId = Location_헤더에서_id값을_꺼낸다(reviewSaveResponse);

        ExtractableResponse<Response> inventoryProductResponse = 로그인된_상태로_GET_요청을_보낸다(
                "/api/v1/members/inventoryProducts", token);
        Long inventoryProductId = inventoryProductResponse.as(InventoryProductsResponse.class)
                .getItems()
                .get(0)
                .getId();

        // when
        ExtractableResponse<Response> response = GET_요청을_보낸다(
                "/api/v1/inventoryProducts/" + inventoryProductId + "/reviews");

        // then
        ReviewWithProductResponse reviewResponse = response.as(ReviewWithProductResponse.class);
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(reviewResponse.getProduct()).usingRecursiveComparison()
                        .ignoringFields("rating", "reviewCount")
                        .isEqualTo(ProductResponse.from(keyboard)),
                () -> assertThat(reviewResponse).usingRecursiveComparison()
                        .ignoringFields("product", "createdAt")
                        .isEqualTo(ReviewWithProductResponse.from(REVIEW_RATING_5.작성(reviewId, keyboard, MINCHO.생성())))
        );
    }

    private Long Location_헤더에서_id값을_꺼낸다(ExtractableResponse<Response> reviewCreateResponse) {
        return Long.parseLong(reviewCreateResponse
                .header("Location")
                .split("/")[4]);
    }

    private Product 제품을_저장한다(Product product) {
        return productRepository.save(product);
    }

    private ReviewWithAuthorResponse 리뷰_아이디에_해당하는_리뷰와_회원정보를_찾는다(ReviewWithAuthorPageResponse reviewPageResponse,
                                                                Long reviewId) {
        return reviewPageResponse.getItems().stream()
                .filter(it -> it.getId().equals(reviewId))
                .findFirst()
                .get();
    }
}
