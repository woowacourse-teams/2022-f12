package com.woowacourse.f12.application.auth.token;

import com.woowacourse.f12.exception.unauthorized.DuplicatedRefreshTokenSavedException;
import com.woowacourse.f12.exception.unauthorized.RefreshTokenNotFoundException;
import com.woowacourse.f12.exception.unauthorized.TooManyRefreshTokenAffectedException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
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

    @Override
    public RefreshToken findToken(final String savedTokenValue) {
        final String sql = "SELECT token_value, expired_at, member_id FROM refresh_token where token_value = ?";
        try {
            return namedParameterJdbcTemplate.getJdbcTemplate()
                    .queryForObject(sql, rowMapper, savedTokenValue);
        } catch (EmptyResultDataAccessException e) {
            throw new RefreshTokenNotFoundException();
        } catch (IncorrectResultSizeDataAccessException e) {
            throw new DuplicatedRefreshTokenSavedException();
        }
    }

    @Override
    public void delete(final String savedTokenValue) {
        final String sql = "DELETE FROM refresh_token where token_value = ?";
        final int update = namedParameterJdbcTemplate.getJdbcTemplate()
                .update(sql, savedTokenValue);
        validateAffectedRowCountIsOne(update);
    }

    private void validateAffectedRowCountIsOne(final int update) {
        if (update != 1) {
            throw new TooManyRefreshTokenAffectedException();
        }
    }
}
