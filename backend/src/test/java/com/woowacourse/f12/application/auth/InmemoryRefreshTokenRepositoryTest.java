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
        LocalDateTime expireAt = LocalDateTime.now().plusWeeks(2);

        // when
        String savedToken = refreshTokenRepository.save("refreshTokenValue", new RefreshTokenInfo("1", expireAt));

        // then
        assertThat(savedToken).isEqualTo("refreshTokenValue");
    }

    @Test
    void 토큰_정보를_찾는다() {
        // given
        LocalDateTime expireAt = LocalDateTime.now().plusWeeks(2);
        RefreshTokenInfo expected = new RefreshTokenInfo("1", expireAt);
        String savedToken = refreshTokenRepository.save("refreshTokenValue", expected);

        // when
        Optional<RefreshTokenInfo> tokenInfo = refreshTokenRepository.findTokenInfo(savedToken);

        //then
        assertThat(tokenInfo.get()).isEqualTo(expected);
    }
}
