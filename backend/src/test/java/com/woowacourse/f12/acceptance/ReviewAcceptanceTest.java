package com.woowacourse.f12.acceptance;

import static com.woowacourse.f12.acceptance.support.LoginUtil.로그인을_한다;
import static com.woowacourse.f12.acceptance.support.RestAssuredRequestUtil.GET_요청을_보낸다;
import static com.woowacourse.f12.acceptance.support.RestAssuredRequestUtil.로그인된_상태로_DELETE_요청을_보낸다;
import static com.woowacourse.f12.acceptance.support.RestAssuredRequestUtil.로그인된_상태로_PUT_요청을_보낸다;
import static com.woowacourse.f12.support.GitHubProfileFixtures.CORINNE_GITHUB;
import static com.woowacourse.f12.support.GitHubProfileFixtures.MINCHO_GITHUB;
import static com.woowacourse.f12.support.ProductFixture.KEYBOARD_1;
import static com.woowacourse.f12.support.ProductFixture.KEYBOARD_2;
import static com.woowacourse.f12.support.ReviewFixtures.REVIEW_RATING_4;
import static com.woowacourse.f12.support.ReviewFixtures.REVIEW_RATING_5;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.fail;

import com.woowacourse.f12.domain.product.Product;
import com.woowacourse.f12.domain.product.ProductRepository;
import com.woowacourse.f12.dto.request.review.ReviewRequest;
import com.woowacourse.f12.dto.response.ExceptionResponse;
import com.woowacourse.f12.dto.response.review.ReviewPageResponse;
import com.woowacourse.f12.dto.response.review.ReviewWithProductPageResponse;
import com.woowacourse.f12.dto.response.review.ReviewWithProductResponse;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

public class ReviewAcceptanceTest extends AcceptanceTest {

    @Autowired
    private ProductRepository productRepository;

    @Test
    void 실패() {
        fail();
    }

    @Test
    void 키보드가_저장되어있고_키보드에_대한_리뷰를_작성한다() {
        // given
        Product product = 키보드를_저장한다(KEYBOARD_1.생성());
        String token = 로그인을_한다(CORINNE_GITHUB.getCode()).getToken();

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
        Product product = 키보드를_저장한다(KEYBOARD_1.생성());
        String token = 로그인을_한다(CORINNE_GITHUB.getCode()).getToken();
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
    void 특정_제품_리뷰_목록을_최신순으로_조회한다() {
        // given
        Product product = 키보드를_저장한다(KEYBOARD_1.생성());
        String token = 로그인을_한다(CORINNE_GITHUB.getCode()).getToken();
        REVIEW_RATING_5.작성_요청을_보낸다(product.getId(), token);
        String token2 = 로그인을_한다(MINCHO_GITHUB.getCode()).getToken();
        Long reviewId = Location_헤더에서_id값을_꺼낸다(REVIEW_RATING_5.작성_요청을_보낸다(product.getId(), token2));

        // when
        String url = "/api/v1/products/" + product.getId() + "/reviews?size=1&page=0&sort=createdAt,desc";
        ExtractableResponse<Response> response = GET_요청을_보낸다(url);

        // then
        ReviewPageResponse reviewPageResponse = response.as(ReviewPageResponse.class);
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(reviewPageResponse.getItems())
                        .extracting("id")
                        .containsExactly(reviewId),
                () -> assertThat(reviewPageResponse.isHasNext()).isTrue()
        );
    }

    @Test
    void 특정_제품_리뷰_목록을_평점순으로_조회한다() {
        // given
        Product product = 키보드를_저장한다(KEYBOARD_1.생성());
        String token = 로그인을_한다(CORINNE_GITHUB.getCode()).getToken();
        REVIEW_RATING_4.작성_요청을_보낸다(product.getId(), token);
        String token2 = 로그인을_한다(MINCHO_GITHUB.getCode()).getToken();
        Long reviewId = Location_헤더에서_id값을_꺼낸다(REVIEW_RATING_5.작성_요청을_보낸다(product.getId(), token2));

        // when
        String url = "/api/v1/products/" + product.getId() + "/reviews?size=1&page=0&sort=rating,desc";
        ExtractableResponse<Response> response = GET_요청을_보낸다(url);

        // then
        ReviewPageResponse reviewPageResponse = response.as(ReviewPageResponse.class);
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(reviewPageResponse.getItems())
                        .extracting("id")
                        .containsExactly(reviewId),
                () -> assertThat(reviewPageResponse.isHasNext()).isTrue()
        );
    }

    @Test
    void 전체_리뷰_목록을_최신순으로_조회한다() {
        // given
        Product product1 = 키보드를_저장한다(KEYBOARD_1.생성());
        Product product2 = 키보드를_저장한다(KEYBOARD_2.생성());
        String token = 로그인을_한다(CORINNE_GITHUB.getCode()).getToken();
        Long reviewId1 = Location_헤더에서_id값을_꺼낸다(REVIEW_RATING_4.작성_요청을_보낸다(product1.getId(), token));
        Long reviewId2 = Location_헤더에서_id값을_꺼낸다(REVIEW_RATING_4.작성_요청을_보낸다(product2.getId(), token));

        // when
        ExtractableResponse<Response> response = GET_요청을_보낸다(
                "/api/v1/reviews?page=0&size=2&sort=createdAt,desc");

        // then
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(response.as(ReviewWithProductPageResponse.class).isHasNext()).isFalse(),
                () -> assertThat(response.as(ReviewWithProductPageResponse.class).getItems())
                        .extracting("id")
                        .containsExactly(reviewId2, reviewId1)
        );
    }

    @Test
    void 로그인한_회원이_리뷰_작성자와_일치하면_리뷰를_수정한다() {
        // given
        Product product = 키보드를_저장한다(KEYBOARD_1.생성());
        String token = 로그인을_한다(CORINNE_GITHUB.getCode()).getToken();
        long reviewId = Location_헤더에서_id값을_꺼낸다(REVIEW_RATING_5.작성_요청을_보낸다(product.getId(), token));
        ReviewRequest requestBody = new ReviewRequest("수정된 내용", 4);

        // when
        ExtractableResponse<Response> response = 로그인된_상태로_PUT_요청을_보낸다("/api/v1/reviews/" + reviewId, token,
                requestBody);
        ReviewWithProductResponse review = GET_요청을_보낸다("/api/v1/reviews?page=0&size=2&sort=createdAt,desc")
                .as(ReviewWithProductPageResponse.class)
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
    void 로그인한_회원이_리뷰_작성자와_일치하면_리뷰를_삭제한다() {
        // given
        Product product = 키보드를_저장한다(KEYBOARD_1.생성());
        String token = 로그인을_한다(CORINNE_GITHUB.getCode()).getToken();
        long reviewId = Location_헤더에서_id값을_꺼낸다(REVIEW_RATING_5.작성_요청을_보낸다(product.getId(), token));

        // when
        ExtractableResponse<Response> response = 로그인된_상태로_DELETE_요청을_보낸다(
                "/api/v1/reviews/" + reviewId, token);
        List<ReviewWithProductResponse> reviews = GET_요청을_보낸다("/api/v1/reviews?page=0&size=2&sort=createdAt,desc")
                .as(ReviewWithProductPageResponse.class)
                .getItems();

        // then
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value()),
                () -> assertThat(reviews).isEmpty()
        );
    }

    private long Location_헤더에서_id값을_꺼낸다(final ExtractableResponse<Response> reviewCreateResponse) {
        return Long.parseLong(reviewCreateResponse
                .header("Location")
                .split("/")[4]);
    }

    private Product 키보드를_저장한다(Product product) {
        return productRepository.save(product);
    }
}
