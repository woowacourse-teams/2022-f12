package com.woowacourse.f12.application.auth;

import java.util.Optional;

public interface RefreshTokenRepository {

    String save(String token, RefreshTokenInfo info);

    Optional<RefreshTokenInfo> findTokenInfo(String savedToken);
}
