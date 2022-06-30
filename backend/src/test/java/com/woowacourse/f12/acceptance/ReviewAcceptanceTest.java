package com.woowacourse.f12.acceptance;

import static com.woowacourse.f12.acceptance.support.RestAssuredRequestUtil.POST_요청을_보낸다;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.woowacourse.f12.domain.Keyboard;
import com.woowacourse.f12.domain.KeyboardRepository;
import com.woowacourse.f12.dto.request.ReviewRequest;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

public class ReviewAcceptanceTest extends AcceptanceTest {

    @Autowired
    private KeyboardRepository keyboardRepository;

    @Test
    void 리뷰를_작성한다() {
        // given
        Keyboard keyboard = 키보드를_저장한다();

        // when
        ReviewRequest reviewRequest = new ReviewRequest("리뷰 내용", 4);
        String url = "/api/v1/keyboards/" + keyboard.getId() + "/reviews";
        ExtractableResponse<Response> response = POST_요청을_보낸다(url, reviewRequest);

        // then
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value()),
                () -> assertThat(response.header("Location")).contains("/api/v1/reviews/")
        );
    }

    private Keyboard 키보드를_저장한다() {
        Keyboard keyboard = Keyboard.builder()
                .name("키보드")
                .build();
        keyboardRepository.save(keyboard);
        return keyboard;
    }
}
