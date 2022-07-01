package com.woowacourse.f12.acceptance;

import static com.woowacourse.f12.acceptance.support.RestAssuredRequestUtil.GET_요청을_보낸다;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.woowacourse.f12.domain.Keyboard;
import com.woowacourse.f12.domain.KeyboardRepository;
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
}
