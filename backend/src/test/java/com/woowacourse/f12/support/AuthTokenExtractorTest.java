package com.woowacourse.f12.support;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.woowacourse.f12.exception.unauthorized.TokenInvalidFormatException;
import com.woowacourse.f12.exception.unauthorized.TokenNotExistsException;
import org.junit.jupiter.api.Test;

class AuthTokenExtractorTest {

    private final AuthTokenExtractor authTokenExtractor = new AuthTokenExtractor();

    @Test
    void Authorization_헤더에서_토큰을_추출한다() {
        // given
        String authorizationHeader = "Bearer token";

        // when
        String token = authTokenExtractor.extractToken(authorizationHeader, "Bearer");

        // then
        assertThat(token).isEqualTo("token");
    }

    @Test
    void Authorization_헤더가_null_이면_예외가_발생한다() {
        assertThatThrownBy(() -> authTokenExtractor.extractToken(null, "Bearer"))
                .isExactlyInstanceOf(TokenNotExistsException.class);
    }

    @Test
    void Authorization_헤더의_타입이_불일치하면_예외가_발생한다() {
        // given
        String authorizationHeader = "NotBearer token";

        // when, then
        assertThatThrownBy(() -> authTokenExtractor.extractToken(authorizationHeader, "Bearer"))
                .isExactlyInstanceOf(TokenInvalidFormatException.class);
    }

    @Test
    void Authorization_헤더의_형식이_불일치하면_예외가_발생한다() {
        // given
        String authorizationHeader = "Bearer token token2";

        // when, then
        assertThatThrownBy(() -> authTokenExtractor.extractToken(authorizationHeader, "Bearer"))
                .isExactlyInstanceOf(TokenInvalidFormatException.class);
    }
}
