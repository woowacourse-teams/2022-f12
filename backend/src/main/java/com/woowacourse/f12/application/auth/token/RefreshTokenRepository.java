package com.woowacourse.f12.application.auth.token;

public interface RefreshTokenRepository {

    RefreshToken save(RefreshToken refreshToken);

   RefreshToken findToken(String savedTokenValue);

    void delete(String savedTokenValue);
}
