package com.woowacourse.f12.application.auth;

public interface RefreshTokenRepository {

    String save(String token, RefreshTokenInfo info);
}
