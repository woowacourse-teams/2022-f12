package com.woowacourse.f12.application.auth;

import static org.assertj.core.api.Assertions.assertThat;

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
        final RefreshTokenInfo refreshTokenInfo = RefreshTokenInfo.createByExpiredDay(1L, 14);

        // when
        String savedToken = refreshTokenRepository.save("refreshTokenValue", refreshTokenInfo);

        // then
        assertThat(savedToken).isEqualTo("refreshTokenValue");
    }

    @Test
    void 토큰_정보를_찾는다() {
        // given
        RefreshTokenInfo expected = RefreshTokenInfo.createByExpiredDay(1L, 14);
        String savedToken = refreshTokenRepository.save("refreshTokenValue", expected);

        // when
        Optional<RefreshTokenInfo> tokenInfo = refreshTokenRepository.findTokenInfo(savedToken);

        //then
        assertThat(tokenInfo.get()).isEqualTo(expected);
    }
}
