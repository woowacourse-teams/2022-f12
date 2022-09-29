package com.woowacourse.f12.application.auth.token;

import com.woowacourse.f12.domain.member.Role;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.Map;
import org.springframework.beans.factory.annotation.Value;

public class FakeJwtProvider {

    private static final String ACCESS_TOKEN_SUBJECT = "AccessToken";

    private final Key secretKey;
    private final long validityInMilliseconds;

    public FakeJwtProvider(@Value("${security.jwt.secret-key}") final String secretKey,
                           @Value("${security.jwt.expire-length}") final long validityInMilliseconds) {
        this.secretKey = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
        this.validityInMilliseconds = validityInMilliseconds;
    }

    public String createAccessToken(final String id, final Role role) {
        final Date now = new Date();
        final Date validity = new Date(now.getTime() + validityInMilliseconds);
        final Map<String, Object> claims = Map.of("id", id, "role", role);

        return Jwts.builder()
                .setSubject(ACCESS_TOKEN_SUBJECT)
                .setIssuedAt(now)
                .setExpiration(validity)
                .addClaims(claims)
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
    }
}
