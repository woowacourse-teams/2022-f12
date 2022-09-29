package com.woowacourse.f12.application.auth.token;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import org.junit.jupiter.api.Test;

class RefreshTokenProviderTest {

    @Test
    void 리프레시_토큰_정보를_생성한다() {
        // given
        RefreshTokenProvider provider = new RefreshTokenProvider(1209600000);

        // when
        RefreshToken token = provider.createToken(1L);

        //then
        assertAll(
                () -> assertThat(token.isExpired()).isFalse(),
                () -> assertThat(token.getRefreshToken()).isNotNull()
        );
    }
}
