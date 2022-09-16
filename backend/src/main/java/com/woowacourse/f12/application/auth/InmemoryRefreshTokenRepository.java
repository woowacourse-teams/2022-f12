package com.woowacourse.f12.application.auth;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Component;

@Component
public class InmemoryRefreshTokenRepository implements RefreshTokenRepository {

    private final Map<String, RefreshTokenInfo> tokens = new ConcurrentHashMap<>();

    @Override
    public String save(final String token, final RefreshTokenInfo info) {
        tokens.put(token, info);
        return token;
    }

    @Override
    public Optional<RefreshTokenInfo> findTokenInfo(final String token) {
        return Optional.ofNullable(tokens.get(token));
    }

    @Override
    public void delete(final String token) {
        tokens.remove(token);
    }
}
