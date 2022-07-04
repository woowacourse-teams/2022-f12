package com.woowacourse.f12.acceptance;

import static com.woowacourse.f12.acceptance.support.RestAssuredRequestUtil.GET_요청을_보낸다;
import static com.woowacourse.f12.acceptance.support.RestAssuredRequestUtil.POST_요청을_보낸다;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.woowacourse.f12.domain.Keyboard;
import com.woowacourse.f12.domain.KeyboardRepository;
import com.woowacourse.f12.dto.request.ReviewRequest;
import com.woowacourse.f12.dto.response.ReviewPageResponse;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

public class ReviewAcceptanceTest extends AcceptanceTest {

    @Autowired
    private KeyboardRepository keyboardRepository;

    @Test
    void 키보드가_저장되어있고_키보드에_대한_리뷰를_작성한다() {
        // given
        Keyboard keyboard = 키보드를_저장한다("키보드", "이미지 URL");

        // when
        ExtractableResponse<Response> response = 키보드에_대한_리뷰를_작성한다(keyboard, 5);

        // then
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value()),
                () -> assertThat(response.header("Location")).contains("/api/v1/reviews/")
        );
    }

    @Test
    void 특정_제품_리뷰_목록을_최신순으로_조회한다() {
        // given
        Keyboard keyboard = 키보드를_저장한다("키보드", "이미지 URL");
        키보드에_대한_리뷰를_작성한다(keyboard, 5);
        Long reviewId = Long.parseLong(키보드에_대한_리뷰를_작성한다(keyboard, 5)
                .header("Location")
                .split("/")[4]);

        // when
        String url = "/api/v1/keyboards/" + keyboard.getId() + "/reviews?size=1&page=0&sort=createdAt,desc";
        ExtractableResponse<Response> response = GET_요청을_보낸다(url);

        // then
        ReviewPageResponse reviewPageResponse = response.as(ReviewPageResponse.class);
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(reviewPageResponse.getReviews())
                        .extracting("id")
                        .containsExactly(reviewId),
                () -> assertThat(reviewPageResponse.isHasNext()).isTrue()
        );
    }

    @Test
    void 특정_제품_리뷰_목록을_평점순으로_조회한다() {
        // given
        Keyboard keyboard = 키보드를_저장한다("키보드", "이미지 URL");
        키보드에_대한_리뷰를_작성한다(keyboard, 4);
        Long reviewId = Long.parseLong(키보드에_대한_리뷰를_작성한다(keyboard, 5)
                .header("Location")
                .split("/")[4]);

        // when
        String url = "/api/v1/keyboards/" + keyboard.getId() + "/reviews?size=1&page=0&sort=rating,desc";
        ExtractableResponse<Response> response = GET_요청을_보낸다(url);

        // then
        ReviewPageResponse reviewPageResponse = response.as(ReviewPageResponse.class);
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(reviewPageResponse.getReviews())
                        .extracting("id")
                        .containsExactly(reviewId),
                () -> assertThat(reviewPageResponse.isHasNext()).isTrue()
        );
    }

    @Test
    void 전체_리뷰_목록을_최신순으로_조회한다() {
        // given
        Keyboard keyboard1 = 키보드를_저장한다("키보드1", "이미지 URL");
        Keyboard keyboard2 = 키보드를_저장한다("키보드2", "이미지 URL");
        Long reviewId1 = Long.parseLong(키보드에_대한_리뷰를_작성한다(keyboard1, 4)
                .header("Location")
                .split("/")[4]);
        Long reviewId2 = Long.parseLong(키보드에_대한_리뷰를_작성한다(keyboard2, 4)
                .header("Location")
                .split("/")[4]);

        // when
        ExtractableResponse<Response> response = GET_요청을_보낸다(
                "/api/v1/keyboards/reviews?page=0&size=2&sort=createdAt,desc");

        // then
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(response.as(ReviewPageResponse.class).isHasNext()).isFalse(),
                () -> assertThat(response.as(ReviewPageResponse.class).getReviews())
                        .extracting("id")
                        .containsExactly(reviewId2, reviewId1)
        );
    }

    private Keyboard 키보드를_저장한다(String name, String imageUrl) {
        Keyboard keyboard = Keyboard.builder()
                .name(name)
                .imageUrl(imageUrl)
                .build();
        keyboardRepository.save(keyboard);
        return keyboard;
    }

    private ExtractableResponse<Response> 키보드에_대한_리뷰를_작성한다(final Keyboard keyboard, final int rating) {
        ReviewRequest reviewRequest = new ReviewRequest("리뷰 내용", rating);
        String url = "/api/v1/keyboards/" + keyboard.getId() + "/reviews";
        return POST_요청을_보낸다(url, reviewRequest);
    }
}
