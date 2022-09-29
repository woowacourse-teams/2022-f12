package com.woowacourse.f12.application.auth;

import static org.assertj.core.api.Assertions.assertThat;

import com.woowacourse.f12.application.auth.token.JwtProvider;
import com.woowacourse.f12.application.auth.token.MemberPayload;
import com.woowacourse.f12.domain.member.Role;
import com.woowacourse.f12.support.AuthTokenExtractor;
import org.junit.jupiter.api.Test;

class JwtProviderTest {

    private final JwtProvider jwtProvider = new JwtProvider(new AuthTokenExtractor(),
            "testadsddersrsfsddsasdfaefasfkk2313123113trssttrs",
            3600000);

    @Test
    void 토큰을_생성한다() {
        // given
        Long memberId = 1L;

        // when
        String token = jwtProvider.createAccessToken(memberId, Role.USER);

        // then
        assertThat(token).isNotNull();
    }

    @Test
    void 토큰이_유효한_경우() {
        // given
        String token = jwtProvider.createAccessToken(1L, Role.USER);
        String authorizationHeader = "Bearer " + token;

        // when, then
        assertThat(jwtProvider.isValidToken(authorizationHeader)).isTrue();
    }

    @Test
    void 토큰의_유효기간이_지난_경우() {
        // given
        JwtProvider jwtProvider = new JwtProvider(new AuthTokenExtractor(),
                "testadsddersrsfsddsasdfaefasfkk2313123113trssttrs",
                0);
        String token = jwtProvider.createAccessToken(1L, Role.USER);
        String authorizationHeader = "Bearer " + token;

        // when, then
        assertThat(jwtProvider.isValidToken(authorizationHeader)).isFalse();
    }

    @Test
    void 토큰의_형식이_틀린_경우() {
        // given
        String authorizationHeader = "Bearer invalidToken";

        // when, then
        assertThat(jwtProvider.isValidToken(authorizationHeader)).isFalse();
    }

    @Test
    void 토큰의_시크릿_키가_틀린_경우() {
        // given
        JwtProvider invalidJwtProvider = new JwtProvider(new AuthTokenExtractor(),
                "invalidlasndflkslflkasnf12sdfasdfasdfa",
                10000000);
        String token = invalidJwtProvider.createAccessToken(1L, Role.USER);
        String authorizationHeader = "Bearer " + token;

        // when, then
        assertThat(jwtProvider.isValidToken(authorizationHeader)).isFalse();
    }

    @Test
    void 토큰의_payload를_복호화한다() {
        // given
        String token = jwtProvider.createAccessToken(1L, Role.USER);
        String authorizationHeader = "Bearer " + token;

        // when
        MemberPayload payload = jwtProvider.getPayload(authorizationHeader);

        // then
        assertThat(payload).isEqualTo(new MemberPayload(1L, Role.USER));
    }
}
