package com.woowacourse.f12.application.auth;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class InmemoryRefreshTokenRepositoryTest {

    private RefreshTokenRepository refreshTokenRepository;

    @BeforeEach
    void setUp() {
        refreshTokenRepository = new InmemoryRefreshTokenRepository();
    }

    @Test
    void 리프레시_토큰을_저장한다() {
        // given
        RefreshToken refreshToken = new RefreshToken("refreshTokenValue", 1L, LocalDateTime.now().plusWeeks(2));

        // when
        RefreshToken savedToken = refreshTokenRepository.save(refreshToken);

        // then
        assertThat(savedToken.getRefreshToken()).isEqualTo("refreshTokenValue");
    }

    @Test
    void 토큰_정보를_찾는다() {
        // given
        RefreshToken refreshToken = new RefreshToken("refreshTokenValue", 1L, LocalDateTime.now().plusWeeks(2));
        RefreshToken savedToken = refreshTokenRepository.save(refreshToken);

        // when
        Optional<RefreshToken> actual = refreshTokenRepository.findToken(savedToken.getRefreshToken());

        //then
        assertThat(actual.get()).isEqualTo(savedToken);
    }
}
