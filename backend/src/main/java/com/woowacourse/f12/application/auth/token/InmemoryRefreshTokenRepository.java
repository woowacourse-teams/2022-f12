package com.woowacourse.f12.application.auth.token;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Component;

@Component
public class InmemoryRefreshTokenRepository implements RefreshTokenRepository {

    private final Map<String, RefreshToken> tokens = new ConcurrentHashMap<>();

    @Override
    public RefreshToken save(final RefreshToken refreshToken) {
        tokens.put(refreshToken.getRefreshToken(), refreshToken);
        return refreshToken;
    }

    @Override
    public Optional<RefreshToken> findToken(final String token) {
        return Optional.ofNullable(tokens.get(token));
    }

    @Override
    public void delete(final String token) {
        tokens.remove(token);
    }
}
