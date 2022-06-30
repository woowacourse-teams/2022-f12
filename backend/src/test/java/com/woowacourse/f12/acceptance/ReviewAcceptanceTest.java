package com.woowacourse.f12.acceptance;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.woowacourse.f12.domain.Keyboard;
import com.woowacourse.f12.domain.KeyboardRepository;
import com.woowacourse.f12.dto.request.ReviewRequest;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
public class ReviewAcceptanceTest {

    @LocalServerPort
    private int port;

    @Autowired
    private KeyboardRepository keyboardRepository;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

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

    private ExtractableResponse<Response> POST_요청을_보낸다(final String url, final Object requestBody) {
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(requestBody)
                .when()
                .post(url)
                .then().log().all()
                .extract();
    }
}
