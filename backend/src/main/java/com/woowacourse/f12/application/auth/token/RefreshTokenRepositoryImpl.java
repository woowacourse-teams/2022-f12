package com.woowacourse.f12.application.auth.token;

import java.util.Optional;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Repository
public class RefreshTokenRepositoryImpl implements RefreshTokenRepository {

    private final RowMapper<RefreshToken> rowMapper = (rs, rowNum) -> RefreshToken.builder()
            .refreshToken(rs.getString("token_value"))
            .expiredAt(rs.getTimestamp("expired_at").toLocalDateTime())
            .memberId(rs.getLong("member_id"))
            .build();

    private final JdbcTemplate jdbcTemplate;

    public RefreshTokenRepositoryImpl(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public RefreshToken save(final RefreshToken refreshToken) {
        final String sql = "INSERT INTO refresh_token (token_value, expired_at, member_id) VALUES (?, ?, ?)";
        final int affectedRowCounts = jdbcTemplate.update(sql, refreshToken.getRefreshToken(),
                refreshToken.getExpiredAt(), refreshToken.getMemberId());
        validateAffectedRowCountIsOne(affectedRowCounts);
        return refreshToken;
    }

    @Override
    public Optional<RefreshToken> findToken(final String savedTokenValue) {
        final String sql = "SELECT token_value, expired_at, member_id FROM refresh_token where token_value = ?";
        try {
            final RefreshToken result = jdbcTemplate.queryForObject(sql, rowMapper, savedTokenValue);
            return Optional.of(result);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public void delete(final String savedTokenValue) {
        final String sql = "DELETE FROM refresh_token where token_value = ?";
        final int update = jdbcTemplate.update(sql, savedTokenValue);
        validateAffectedRowCountIsOne(update);
    }

    private void validateAffectedRowCountIsOne(final int update) {
        if (update != 1) {
            throw new IllegalArgumentException();
        }
    }
}
