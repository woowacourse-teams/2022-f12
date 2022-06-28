package com.woowacourse.f12.keyboard.acceptance;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.woowacourse.f12.keyboard.domain.Keyboard;
import com.woowacourse.f12.keyboard.domain.KeyboardRepository;
import com.woowacourse.f12.keyboard.dto.response.KeyboardResponse;
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
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
class KeyboardAcceptanceTest {

    @LocalServerPort
    private int port;

    @Autowired
    private KeyboardRepository keyboardRepository;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @Test
    void 키보드_단일_제품_조회한다() {
        // given
        Keyboard keyboard = 키보드를_저장한다();

        // when
        ExtractableResponse<Response> response = GET_요청을_보낸다("/api/v1/keyboards/" + keyboard.getId());

        // then
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(response.as(KeyboardResponse.class)).usingRecursiveComparison()
                        .isEqualTo(keyboard)
        );
    }

    private Keyboard 키보드를_저장한다() {
        Keyboard keyboard = Keyboard.builder()
                .name("키보드")
                .build();
        keyboardRepository.save(keyboard);
        return keyboard;
    }

    private ExtractableResponse<Response> GET_요청을_보낸다(final String url) {
        return RestAssured.given().log().all()
                .when()
                .get(url)
                .then().log().all()
                .extract();
    }
}
