package com.woowacourse.f12.application.auth.token;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;

@JdbcTest
@Sql("classpath:refreshTokenDDL.sql")
class RefreshTokenRepositoryImplTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private RefreshTokenRepositoryImpl refreshTokenRepositoryImpl;

    @BeforeEach
    void setUp() {
        refreshTokenRepositoryImpl = new RefreshTokenRepositoryImpl(jdbcTemplate);
    }

    @Test
    void 리프레시_토큰을_저장한다() {
        // given
        String tokenValue = "tokenValue";
        RefreshToken refreshToken = RefreshToken.builder()
                .refreshToken(tokenValue)
                .expiredAt(LocalDateTime.now())
                .memberId(1L)
                .build();

        // when
        RefreshToken savedRefreshToken = refreshTokenRepositoryImpl.save(refreshToken);

        // then
        assertThat(savedRefreshToken).isEqualTo(refreshToken);
    }

    @Test
    void 리프레시_토큰의_내용을_통해_조회한다() {
        // given
        String tokenValue = "tokenValue";
        RefreshToken refreshToken = RefreshToken.builder()
                .refreshToken(tokenValue)
                .expiredAt(LocalDateTime.now())
                .memberId(1L)
                .build();
        refreshTokenRepositoryImpl.save(refreshToken);

        // when
        final RefreshToken actual = refreshTokenRepositoryImpl.findToken(tokenValue)
                .orElseThrow();

        // then
        assertThat(actual).usingRecursiveComparison()
                .isEqualTo(refreshToken);

    }

    @Test
    void 리프레시_토큰의_내용을_통해_리프레시_토큰을_삭제한다() {
        // given
        String tokenValue = "tokenValue";
        RefreshToken refreshToken = RefreshToken.builder()
                .refreshToken(tokenValue)
                .expiredAt(LocalDateTime.now())
                .memberId(1L)
                .build();
        refreshTokenRepositoryImpl.save(refreshToken);

        // when
        refreshTokenRepositoryImpl.delete(tokenValue);

        // then
        assertThat(refreshTokenRepositoryImpl.findToken(tokenValue)).isEqualTo(Optional.empty());
    }
}
