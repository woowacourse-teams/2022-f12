package com.woowacourse.f12.application.auth.token;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import com.woowacourse.f12.exception.unauthorized.DuplicatedRefreshTokenSavedException;
import com.woowacourse.f12.exception.unauthorized.RefreshTokenNotFoundException;
import com.woowacourse.f12.exception.unauthorized.TooManyRefreshTokenAffectedException;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.test.context.jdbc.Sql;

@JdbcTest
@Sql("classpath:refreshTokenDDL.sql")
class RefreshTokenRepositoryImplTest {

    private final String tokenValue = "tokenValue";
    private final Long memberId = 1L;

    private RefreshTokenRepositoryImpl refreshTokenRepositoryImpl;

    @Autowired
    RefreshTokenRepositoryImplTest(final NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.refreshTokenRepositoryImpl = new RefreshTokenRepositoryImpl(namedParameterJdbcTemplate);
    }

    @Test
    void 리프레시_토큰을_저장한다() {
        // given
        RefreshToken refreshToken = RefreshToken.builder()
                .refreshToken(tokenValue)
                .expiredAt(LocalDateTime.now())
                .memberId(memberId)
                .build();

        // when
        RefreshToken savedRefreshToken = refreshTokenRepositoryImpl.save(refreshToken);

        // then
        assertThat(savedRefreshToken).isEqualTo(refreshToken);
    }

    @Test
    void 리프레시_토큰의_내용을_통해_조회한다() {
        // given
        RefreshToken refreshToken = RefreshToken.builder()
                .refreshToken(tokenValue)
                .expiredAt(LocalDateTime.now())
                .memberId(memberId)
                .build();
        refreshTokenRepositoryImpl.save(refreshToken);

        // when
        RefreshToken actual = refreshTokenRepositoryImpl.findToken(tokenValue);

        // then
        assertThat(actual).isEqualTo(refreshToken);
    }

    @Test
    void 리프레시_토큰의_내용을_통해_리프레시_토큰을_삭제한다() {
        // given
        RefreshToken refreshToken = RefreshToken.builder()
                .refreshToken(tokenValue)
                .expiredAt(LocalDateTime.now())
                .memberId(memberId)
                .build();
        refreshTokenRepositoryImpl.save(refreshToken);

        // when
        refreshTokenRepositoryImpl.delete(tokenValue);

        // then
        assertThatThrownBy(() -> refreshTokenRepositoryImpl.findToken(tokenValue))
                .isExactlyInstanceOf(RefreshTokenNotFoundException.class);
    }

    @Test
    void 조회하려는_리프레시_토큰이_동일한_값의_리프레시_토큰이_여러개_존재하는_경우_예외가_발생한다() {
        // given
        NamedParameterJdbcTemplate mockedNamedTemplate = mock(NamedParameterJdbcTemplate.class);
        given(mockedNamedTemplate.queryForObject(anyString(), any(SqlParameterSource.class), any(RowMapper.class)))
                .willThrow(IncorrectResultSizeDataAccessException.class);

        refreshTokenRepositoryImpl = new RefreshTokenRepositoryImpl(mockedNamedTemplate);

        // when, then
        assertAll(
                () -> assertThatThrownBy(() -> refreshTokenRepositoryImpl.findToken(tokenValue))
                        .isExactlyInstanceOf(DuplicatedRefreshTokenSavedException.class),
                () -> verify(mockedNamedTemplate)
                        .queryForObject(anyString(), any(SqlParameterSource.class), any(RowMapper.class))
        );
    }

    @Test
    void 여러_개의_리프레시_토큰이_등록될_경우_예외가_발생한다() {
        // given
        RefreshToken refreshToken = RefreshToken.builder()
                .refreshToken(tokenValue)
                .memberId(memberId)
                .expiredAt(LocalDateTime.now())
                .build();
        NamedParameterJdbcTemplate mockedNamedTemplate = mock(NamedParameterJdbcTemplate.class);
        given(mockedNamedTemplate.update(anyString(), any(SqlParameterSource.class)))
                .willReturn(2);
        refreshTokenRepositoryImpl = new RefreshTokenRepositoryImpl(mockedNamedTemplate);

        // when, then
        assertAll(
                () -> assertThatThrownBy(() -> refreshTokenRepositoryImpl.save(refreshToken))
                        .isExactlyInstanceOf(TooManyRefreshTokenAffectedException.class),
                () -> verify(mockedNamedTemplate).update(anyString(), any(SqlParameterSource.class))
        );
    }

    @Test
    void 여러_개의_리프레시_토큰이_삭제될_경우_예외가_발생한다() {
        // given
        NamedParameterJdbcTemplate mockedNamedTemplate = mock(NamedParameterJdbcTemplate.class);
        given(mockedNamedTemplate.update(anyString(), any(SqlParameterSource.class)))
                .willReturn(2);
        refreshTokenRepositoryImpl = new RefreshTokenRepositoryImpl(mockedNamedTemplate);

        // when, then
        assertAll(
                () -> assertThatThrownBy(() -> refreshTokenRepositoryImpl.delete(tokenValue))
                        .isExactlyInstanceOf(TooManyRefreshTokenAffectedException.class),
                () -> verify(mockedNamedTemplate).update(anyString(), any(SqlParameterSource.class))
        );
    }
}
