package com.woowacourse.f12.application;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class JwtProviderTest {

    private final JwtProvider jwtProvider = new JwtProvider("testadsddersrsfsddsasdfaefasfkk2313123113trssttrs",
            3600000);

    @Test
    void 토큰을_생성한다() {
        // given
        Long memberId = 1L;

        // when
        String token = jwtProvider.createToken(memberId);

        // then
        assertThat(token).isNotNull();
    }
}
