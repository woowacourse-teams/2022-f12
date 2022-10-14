package com.woowacourse.f12.application.auth.token;

import com.woowacourse.f12.exception.unauthorized.DuplicatedRefreshTokenSavedException;
import com.woowacourse.f12.exception.unauthorized.RefreshTokenNotFoundException;
import com.woowacourse.f12.exception.unauthorized.TooManyRefreshTokenAffectedException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

@Repository
public class RefreshTokenRepositoryImpl implements RefreshTokenRepository {

    private final RowMapper<RefreshToken> rowMapper = (rs, rowNum) -> RefreshToken.builder()
            .refreshToken(rs.getString("token_value"))
            .expiredAt(rs.getTimestamp("expired_at").toLocalDateTime())
            .memberId(rs.getLong("member_id"))
            .build();

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public RefreshTokenRepositoryImpl(final NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    @Override
    public RefreshToken save(final RefreshToken refreshToken) {
        final String sql = "INSERT INTO refresh_token (token_value, expired_at, member_id) VALUES (:refreshToken, :expiredAt, :memberId)";
        final BeanPropertySqlParameterSource parameterSource = new BeanPropertySqlParameterSource(refreshToken);
        final int affectedRowCounts = namedParameterJdbcTemplate.update(sql, parameterSource);
        validateAffectedRowCountIsOne(affectedRowCounts);
        return refreshToken;
    }

    private void validateAffectedRowCountIsOne(final int update) {
        if (update != 1) {
            throw new TooManyRefreshTokenAffectedException();
        }
    }

    @Override
    public RefreshToken findToken(final String savedTokenValue) {
        final String sql = "SELECT token_value, expired_at, member_id FROM refresh_token where token_value = :token_value";
        try {
            SqlParameterSource namedParameters = new MapSqlParameterSource("token_value", savedTokenValue);
            return namedParameterJdbcTemplate.queryForObject(sql, namedParameters, rowMapper);
        } catch (EmptyResultDataAccessException e) {
            throw new RefreshTokenNotFoundException();
        } catch (IncorrectResultSizeDataAccessException e) {
            throw new DuplicatedRefreshTokenSavedException();
        }
    }

    @Override
    public void delete(final String savedTokenValue) {
        final String sql = "DELETE FROM refresh_token where token_value = :token_value";
        SqlParameterSource namedParameters = new MapSqlParameterSource("token_value", savedTokenValue);
        final int update = namedParameterJdbcTemplate.update(sql, namedParameters);
        validateAffectedRowCountIsOne(update);
    }
}
