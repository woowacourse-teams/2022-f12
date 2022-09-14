package com.woowacourse.f12.presentation.auth;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.woowacourse.f12.exception.unauthorized.TokenInvalidException;
import org.junit.jupiter.api.Test;

class LoginPayloadTest {

    @Test
    void payload_문자열을_파싱한다() {
        // given
        String payload = "1;USER";

        // when
        LoginPayload loginPayload = LoginPayload.from(payload);

        // then
        assertThat(loginPayload).isNotNull();
    }

    @Test
    void id_부분이_숫자가_아닌_payload가_들어오면_예외를_반환한다() {
        // given
        String payload = "notNum;USER";

        // when, then
        assertThatThrownBy(() -> LoginPayload.from(payload))
                .isExactlyInstanceOf(TokenInvalidException.class);
    }

    @Test
    void Role_부분이_올바르지_않은_payload가_들어오면_예외를_반환한다() {
        // given
        String payload = "1;INVALID";

        // when, then
        assertThatThrownBy(() -> LoginPayload.from(payload))
                .isExactlyInstanceOf(TokenInvalidException.class);
    }
}
