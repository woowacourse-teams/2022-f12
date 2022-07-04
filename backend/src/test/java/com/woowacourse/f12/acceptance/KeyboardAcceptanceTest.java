package com.woowacourse.f12.acceptance;

import static com.woowacourse.f12.acceptance.support.RestAssuredRequestUtil.GET_요청을_보낸다;
import static com.woowacourse.f12.acceptance.support.RestAssuredRequestUtil.POST_요청을_보낸다;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.woowacourse.f12.domain.Keyboard;
import com.woowacourse.f12.domain.KeyboardRepository;
import com.woowacourse.f12.dto.request.ReviewRequest;
import com.woowacourse.f12.dto.response.KeyboardPageResponse;
import com.woowacourse.f12.dto.response.KeyboardResponse;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

class KeyboardAcceptanceTest extends AcceptanceTest {

    @Autowired
    private KeyboardRepository keyboardRepository;

    @Test
    void 키보드_단일_제품_조회한다() {
        // given
        Keyboard keyboard = 키보드를_저장한다("키보드", "이미지 URL");

        // when
        ExtractableResponse<Response> response = GET_요청을_보낸다("/api/v1/keyboards/" + keyboard.getId());

        // then
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(response.as(KeyboardResponse.class)).usingRecursiveComparison()
                        .isEqualTo(keyboard)
        );
    }

    @Test
    void 키보드_목록을_페이징하여_조회한다() {
        // given
        Keyboard keyboard = 키보드를_저장한다("키보드1", "이미지 URL");
        키보드를_저장한다("키보드2", "이미지 URL");

        // when
        ExtractableResponse<Response> response = GET_요청을_보낸다("/api/v1/keyboards?page=0&size=1");

        // then
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(response.as(KeyboardPageResponse.class).getKeyboards())
                        .extracting("id")
                        .containsExactly(keyboard.getId()),
                () -> assertThat(response.as(KeyboardPageResponse.class).isHasNext()).isTrue()
        );
    }

    @Test
    void 키보드_목록을_리뷰가_많은_순서로_페이징하여_조회한다() {
        // given
        Keyboard keyboard1 = 키보드를_저장한다("키보드1", "이미지 URL");
        Keyboard keyboard2 = 키보드를_저장한다("키보드2", "이미지 URL");
        키보드에_대한_리뷰를_작성한다(keyboard1, 5);
        키보드에_대한_리뷰를_작성한다(keyboard1, 4);
        키보드에_대한_리뷰를_작성한다(keyboard2, 3);

        // when
        ExtractableResponse<Response> response = GET_요청을_보낸다("/api/v1/keyboards?page=0&size=1&sort=reviewCount,desc");

        // then
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(response.as(KeyboardPageResponse.class).getKeyboards())
                        .extracting("id")
                        .containsExactly(keyboard1.getId()),
                () -> assertThat(response.as(KeyboardPageResponse.class).isHasNext()).isTrue()
        );
    }

    @Test
    void 키보드_목록을_평점_높은_순서로_페이징하여_조회한다() {
        // given
        Keyboard keyboard1 = 키보드를_저장한다("키보드1", "이미지 URL");
        Keyboard keyboard2 = 키보드를_저장한다("키보드2", "이미지 URL");
        키보드에_대한_리뷰를_작성한다(keyboard1, 5);
        키보드에_대한_리뷰를_작성한다(keyboard1, 1);
        키보드에_대한_리뷰를_작성한다(keyboard2, 4);

        // when
        ExtractableResponse<Response> response = GET_요청을_보낸다("/api/v1/keyboards?page=0&size=1&sort=rating,desc");

        // then
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(response.as(KeyboardPageResponse.class).getKeyboards())
                        .extracting("id")
                        .containsExactly(keyboard2.getId()),
                () -> assertThat(response.as(KeyboardPageResponse.class).isHasNext()).isTrue()
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
