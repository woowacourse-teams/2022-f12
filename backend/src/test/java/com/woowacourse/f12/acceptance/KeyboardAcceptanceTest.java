package com.woowacourse.f12.acceptance;

import static com.woowacourse.f12.acceptance.support.LoginUtil.로그인을_한다;
import static com.woowacourse.f12.acceptance.support.RestAssuredRequestUtil.GET_요청을_보낸다;
import static com.woowacourse.f12.support.KeyboardFixtures.KEYBOARD_1;
import static com.woowacourse.f12.support.KeyboardFixtures.KEYBOARD_2;
import static com.woowacourse.f12.support.ReviewFixtures.REVIEW_RATING_3;
import static com.woowacourse.f12.support.ReviewFixtures.REVIEW_RATING_4;
import static com.woowacourse.f12.support.ReviewFixtures.REVIEW_RATING_5;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.woowacourse.f12.domain.product.Keyboard;
import com.woowacourse.f12.domain.product.KeyboardRepository;
import com.woowacourse.f12.dto.response.product.KeyboardPageResponse;
import com.woowacourse.f12.dto.response.product.KeyboardResponse;
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
        Keyboard keyboard = 키보드를_저장한다(KEYBOARD_1.생성());

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
        Keyboard keyboard = 키보드를_저장한다(KEYBOARD_1.생성());
        키보드를_저장한다(KEYBOARD_2.생성());

        // when
        ExtractableResponse<Response> response = GET_요청을_보낸다("/api/v1/keyboards?page=0&size=1");

        // then
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(response.as(KeyboardPageResponse.class).getItems())
                        .extracting("id")
                        .containsExactly(keyboard.getId()),
                () -> assertThat(response.as(KeyboardPageResponse.class).isHasNext()).isTrue()
        );
    }

    @Test
    void 키보드_목록을_리뷰가_많은_순서로_페이징하여_조회한다() {
        // given
        Keyboard keyboard1 = 키보드를_저장한다(KEYBOARD_1.생성());
        Keyboard keyboard2 = 키보드를_저장한다(KEYBOARD_2.생성());
        String token = 로그인을_한다("1").getToken();
        REVIEW_RATING_5.작성_요청을_보낸다(keyboard1.getId(), token);
        REVIEW_RATING_4.작성_요청을_보낸다(keyboard1.getId(), token);
        REVIEW_RATING_3.작성_요청을_보낸다(keyboard2.getId(), token);

        // when
        ExtractableResponse<Response> response = GET_요청을_보낸다("/api/v1/keyboards?page=0&size=1&sort=reviewCount,desc");

        // then
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(response.as(KeyboardPageResponse.class).getItems())
                        .extracting("id")
                        .containsExactly(keyboard1.getId()),
                () -> assertThat(response.as(KeyboardPageResponse.class).isHasNext()).isTrue()
        );
    }

    @Test
    void 키보드_목록을_평점_높은_순서로_페이징하여_조회한다() {
        // given
        Keyboard keyboard1 = 키보드를_저장한다(KEYBOARD_1.생성());
        Keyboard keyboard2 = 키보드를_저장한다(KEYBOARD_2.생성());
        String token = 로그인을_한다("1").getToken();
        REVIEW_RATING_4.작성_요청을_보낸다(keyboard1.getId(), token);
        REVIEW_RATING_5.작성_요청을_보낸다(keyboard2.getId(), token);

        // when
        ExtractableResponse<Response> response = GET_요청을_보낸다("/api/v1/keyboards?page=0&size=1&sort=rating,desc");

        // then
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(response.as(KeyboardPageResponse.class).getItems())
                        .extracting("id")
                        .containsExactly(keyboard2.getId()),
                () -> assertThat(response.as(KeyboardPageResponse.class).isHasNext()).isTrue()
        );
    }

    private Keyboard 키보드를_저장한다(Keyboard keyboard) {
        return keyboardRepository.save(keyboard);
    }
}
