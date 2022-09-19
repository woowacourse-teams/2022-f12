package com.woowacourse.f12.application.auth;

import java.util.Optional;

public interface RefreshTokenRepository {

    RefreshToken save(RefreshToken refreshToken);

    Optional<RefreshToken> findToken(String savedTokenValue);

    void delete(String savedTokenValue);
}
