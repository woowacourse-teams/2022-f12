package com.woowacourse.f12.acceptance;

import static com.woowacourse.f12.acceptance.support.RestAssuredRequestUtil.GET_요청을_보낸다;
import static com.woowacourse.f12.acceptance.support.RestAssuredRequestUtil.로그인된_상태로_DELETE_요청을_보낸다;
import static com.woowacourse.f12.acceptance.support.RestAssuredRequestUtil.로그인된_상태로_GET_요청을_보낸다;
import static com.woowacourse.f12.acceptance.support.RestAssuredRequestUtil.로그인된_상태로_PUT_요청을_보낸다;
import static com.woowacourse.f12.presentation.member.CareerLevelConstant.SENIOR_CONSTANT;
import static com.woowacourse.f12.presentation.member.JobTypeConstant.BACKEND_CONSTANT;
import static com.woowacourse.f12.support.action.UnauthorizedAction.로그인하지_않고;
import static com.woowacourse.f12.support.fixture.AcceptanceFixture.민초;
import static com.woowacourse.f12.support.fixture.AcceptanceFixture.코린;
import static com.woowacourse.f12.support.fixture.ProductFixture.KEYBOARD_1;
import static com.woowacourse.f12.support.fixture.ProductFixture.KEYBOARD_2;
import static com.woowacourse.f12.support.fixture.ProductFixture.MONITOR_1;
import static com.woowacourse.f12.support.fixture.ProductFixture.MONITOR_2;
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
        MemberRequest memberRequest = new MemberRequest(SENIOR_CONSTANT, BACKEND_CONSTANT);

        String loginToken = 코린.로그인을_한다().getToken();
        코린.로그인한_상태로(loginToken).추가정보를_입력한다(memberRequest);

        // when
        ExtractableResponse<Response> response = 코린.로그인한_상태로(loginToken)
                .리뷰를_작성한다(product.getId(), REVIEW_RATING_5);

        // then
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value()),
                () -> assertThat(response.header("Location")).contains("/api/v1/reviews/")
        );
    }

    @Test
    void 같은_회원이_같은_제품에_리뷰를_중복해서_작성할_수_없다() {
        // given
        Product product = 제품을_저장한다(KEYBOARD_1.생성());
        MemberRequest memberRequest = new MemberRequest(SENIOR_CONSTANT, BACKEND_CONSTANT);

        String loginToken = 코린.로그인을_한다().getToken();
        코린.로그인한_상태로(loginToken).추가정보를_입력한다(memberRequest);
        코린.로그인한_상태로(loginToken).리뷰를_작성한다(product.getId(), REVIEW_RATING_5);

        // when
        ExtractableResponse<Response> response = 코린.로그인한_상태로(loginToken)
                .리뷰를_작성한다(product.getId(), REVIEW_RATING_5);

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

        String token = 코린.로그인을_한다().getToken();
        코린.로그인한_상태로(token).추가정보를_입력한다(memberRequest);
        코린.로그인한_상태로(token).리뷰를_작성한다(product.getId(), REVIEW_RATING_5);

        String token2 = 민초.로그인을_한다().getToken();
        민초.로그인한_상태로(token2).추가정보를_입력한다(memberRequest);
        Long expectedReviewId = Location_헤더에서_id값을_꺼낸다(
                민초.로그인한_상태로(token2).리뷰를_작성한다(product.getId(), REVIEW_RATING_5));

        // when
        String url = "/api/v1/products/" + product.getId() + "/reviews?size=1&page=0&sort=createdAt,desc";
        ExtractableResponse<Response> response = GET_요청을_보낸다(url);

        // then
        ReviewWithAuthorPageResponse reviewPageResponse = response.as(ReviewWithAuthorPageResponse.class);
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(reviewPageResponse.getItems())
                        .extracting("id")
                        .containsExactly(expectedReviewId),
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
        MemberRequest memberRequest = new MemberRequest(SENIOR_CONSTANT, BACKEND_CONSTANT);

        String token = 코린.로그인을_한다().getToken();
        코린.로그인한_상태로(token).추가정보를_입력한다(memberRequest);
        코린.로그인한_상태로(token).리뷰를_작성한다(product.getId(), REVIEW_RATING_4);

        String token2 = 민초.로그인을_한다().getToken();
        민초.로그인한_상태로(token2).추가정보를_입력한다(memberRequest);
        Long expectedReviewId = Location_헤더에서_id값을_꺼낸다(
                민초.로그인한_상태로(token2).리뷰를_작성한다(product.getId(), REVIEW_RATING_5));

        // when
        String url = "/api/v1/products/" + product.getId() + "/reviews?size=1&page=0&sort=rating,desc";
        ExtractableResponse<Response> response = GET_요청을_보낸다(url);

        // then
        ReviewWithAuthorPageResponse reviewPageResponse = response.as(ReviewWithAuthorPageResponse.class);
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(reviewPageResponse.getItems())
                        .extracting("id")
                        .containsExactly(expectedReviewId),
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
        MemberRequest memberRequest = new MemberRequest(SENIOR_CONSTANT, BACKEND_CONSTANT);

        String corinneToken = 코린.로그인을_한다().getToken();
        코린.로그인한_상태로(corinneToken).추가정보를_입력한다(memberRequest);
        Long corinneReviewId = Location_헤더에서_id값을_꺼낸다(
                코린.로그인한_상태로(corinneToken).리뷰를_작성한다(product.getId(), REVIEW_RATING_4));

        String minchoToken = 민초.로그인을_한다().getToken();
        민초.로그인한_상태로(minchoToken).추가정보를_입력한다(memberRequest);
        Long minchoReviewId = Location_헤더에서_id값을_꺼낸다(
                민초.로그인한_상태로(minchoToken).리뷰를_작성한다(product.getId(), REVIEW_RATING_4));

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
        Product product3 = 제품을_저장한다(MONITOR_1.생성());
        Product product4 = 제품을_저장한다(MONITOR_2.생성());
        MemberRequest memberRequest = new MemberRequest(SENIOR_CONSTANT, BACKEND_CONSTANT);

        String loginToken = 코린.로그인을_한다().getToken();
        코린.로그인한_상태로(loginToken).추가정보를_입력한다(memberRequest);

        Long reviewId1 = Location_헤더에서_id값을_꺼낸다(
                코린.로그인한_상태로(loginToken).리뷰를_작성한다(product1.getId(), REVIEW_RATING_4));
        Long reviewId2 = Location_헤더에서_id값을_꺼낸다(
                코린.로그인한_상태로(loginToken).리뷰를_작성한다(product2.getId(), REVIEW_RATING_4));
        Long reviewId3 = Location_헤더에서_id값을_꺼낸다(
                코린.로그인한_상태로(loginToken).리뷰를_작성한다(product3.getId(), REVIEW_RATING_4));
        Location_헤더에서_id값을_꺼낸다(
                코린.로그인한_상태로(loginToken).리뷰를_작성한다(product4.getId(), REVIEW_RATING_4));

        // when
        ExtractableResponse<Response> response = GET_요청을_보낸다(
                "/api/v1/reviews?cursor=" + reviewId3 + "&size=2&sort=id,desc");

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
        MemberRequest memberRequest = new MemberRequest(SENIOR_CONSTANT, BACKEND_CONSTANT);

        String loginToken = 코린.로그인을_한다().getToken();
        코린.로그인한_상태로(loginToken).추가정보를_입력한다(memberRequest);

        Long reviewId = Location_헤더에서_id값을_꺼낸다(코린.로그인한_상태로(loginToken).리뷰를_작성한다(product.getId(), REVIEW_RATING_5));

        // when
        ReviewRequest requestBody = new ReviewRequest("수정된 내용", 4);
        ExtractableResponse<Response> response = 로그인된_상태로_PUT_요청을_보낸다("/api/v1/reviews/" + reviewId, loginToken,
                requestBody);

        // then
        ReviewWithAuthorAndProductResponse updatedReview = 로그인하지_않고().최근_리뷰_목록_첫_페이지를_조회한다(2)
                .as(ReviewWithAuthorAndProductPageResponse.class)
                .getItems().get(0);

        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value()),
                () -> assertThat(updatedReview.getContent()).isEqualTo(requestBody.getContent()),
                () -> assertThat(updatedReview.getRating()).isEqualTo(requestBody.getRating())
        );
    }

    @Test
    void 로그인한_회원이_리뷰_작성자와_일치하면_리뷰를_삭제하고_인벤토리_장비도_삭제한다() {
        // given
        Product product = 제품을_저장한다(KEYBOARD_1.생성());
        MemberRequest memberRequest = new MemberRequest(SENIOR_CONSTANT, BACKEND_CONSTANT);

        String loginToken = 코린.로그인을_한다().getToken();
        코린.로그인한_상태로(loginToken).추가정보를_입력한다(memberRequest);

        Long reviewId = Location_헤더에서_id값을_꺼낸다(코린.로그인한_상태로(loginToken).리뷰를_작성한다(product.getId(), REVIEW_RATING_5));

        // when
        ExtractableResponse<Response> response = 로그인된_상태로_DELETE_요청을_보낸다("/api/v1/reviews/" + reviewId, loginToken);

        // then
        List<ReviewWithAuthorAndProductResponse> reviews = 로그인하지_않고().최근_리뷰_목록_첫_페이지를_조회한다(2)
                .as(ReviewWithAuthorAndProductPageResponse.class)
                .getItems();
        List<InventoryProductResponse> inventoryProducts = 코린.로그인한_상태로(loginToken).자신의_인벤토리를_조회한다()
                .as(InventoryProductsResponse.class)
                .getItems();

        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value()),
                () -> assertThat(reviews).isEmpty(),
                () -> assertThat(inventoryProducts).isEmpty()
        );
    }

    @Test
    void 작성한_리뷰_목록을_다른_회원이나_로그인하지_않고_최신순으로_조회한다() {
        // given
        Product product1 = 제품을_저장한다(KEYBOARD_1.생성());
        Product product2 = 제품을_저장한다(KEYBOARD_2.생성());
        MemberRequest memberRequest = new MemberRequest(SENIOR_CONSTANT, BACKEND_CONSTANT);

        LoginResponse loginResponse = 코린.로그인을_한다();
        String token = loginResponse.getToken();
        코린.로그인한_상태로(token).추가정보를_입력한다(memberRequest);

        Long reviewId1 = Location_헤더에서_id값을_꺼낸다(코린.로그인한_상태로(token).리뷰를_작성한다(product1.getId(), REVIEW_RATING_4));
        Long reviewId2 = Location_헤더에서_id값을_꺼낸다(코린.로그인한_상태로(token).리뷰를_작성한다(product2.getId(), REVIEW_RATING_4));

        // when
        ExtractableResponse<Response> response = GET_요청을_보낸다(
                "/api/v1/members/" + loginResponse.getMember().getId() + "/reviews?size=2&sort=id,desc");

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
    void 내가_작성한_리뷰_목록을_최신순으로_조회한다() {
        // given
        Product product1 = 제품을_저장한다(KEYBOARD_1.생성());
        Product product2 = 제품을_저장한다(KEYBOARD_2.생성());
        MemberRequest memberRequest = new MemberRequest(SENIOR_CONSTANT, BACKEND_CONSTANT);

        String loginToken = 코린.로그인을_한다().getToken();
        코린.로그인한_상태로(loginToken).추가정보를_입력한다(memberRequest);

        Long reviewId1 = Location_헤더에서_id값을_꺼낸다(
                코린.로그인한_상태로(loginToken).리뷰를_작성한다(product1.getId(), REVIEW_RATING_4));
        Long reviewId2 = Location_헤더에서_id값을_꺼낸다(
                코린.로그인한_상태로(loginToken).리뷰를_작성한다(product2.getId(), REVIEW_RATING_4));

        // when
        ExtractableResponse<Response> response = 로그인된_상태로_GET_요청을_보낸다(
                "/api/v1/members/me/reviews?page=0&size=2&sort=createdAt,desc", loginToken);

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
        Product product = 제품을_저장한다(KEYBOARD_1.생성());
        MemberRequest memberRequest = new MemberRequest(SENIOR_CONSTANT, BACKEND_CONSTANT);

        String token = 민초.로그인을_한다().getToken();
        민초.로그인한_상태로(token).추가정보를_입력한다(memberRequest);

        Long reviewId = Location_헤더에서_id값을_꺼낸다(민초.로그인한_상태로(token).리뷰를_작성한다(product.getId(), REVIEW_RATING_5));

        ExtractableResponse<Response> inventoryProductResponse = 민초.로그인한_상태로(token).자신의_인벤토리를_조회한다();
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
                        .isEqualTo(ProductResponse.from(product)),
                () -> assertThat(reviewResponse).usingRecursiveComparison()
                        .ignoringFields("product", "createdAt")
                        .isEqualTo(ReviewWithProductResponse.from(
                                REVIEW_RATING_5.작성(reviewId, product, 민초.엔티티를().생성())))
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
