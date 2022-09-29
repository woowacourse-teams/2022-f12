package com.woowacourse.f12.application.auth.token;

import com.woowacourse.f12.support.AuthTokenExtractor;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtProvider {

    private static final String TOKEN_TYPE = "Bearer";
    private static final String ACCESS_TOKEN_SUBJECT = "AccessToken";

    private final AuthTokenExtractor authTokenExtractor;
    private final Key secretKey;
    private final long validityInMilliseconds;

    public JwtProvider(final AuthTokenExtractor authTokenExtractor,
                       @Value("${security.jwt.secret-key}") final String secretKey,
                       @Value("${security.jwt.expire-length}") final long validityInMilliseconds) {
        this.authTokenExtractor = authTokenExtractor;
        this.secretKey = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
        this.validityInMilliseconds = validityInMilliseconds;
    }

    public String createAccessToken(final Long id) {
        final Date now = new Date();
        final Date validity = new Date(now.getTime() + validityInMilliseconds);

        return Jwts.builder()
                .setSubject(ACCESS_TOKEN_SUBJECT)
                .setIssuedAt(now)
                .setExpiration(validity)
                .claim("id", id.toString())
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
    }

    public boolean isValidToken(final String authorizationHeader) {
        final String token = authTokenExtractor.extractToken(authorizationHeader, TOKEN_TYPE);
        try {
            final Jws<Claims> claims = getClaimsJws(token);
            return isAccessToken(claims) && isNotExpired(claims);
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    private boolean isAccessToken(final Jws<Claims> claims) {
        return claims.getBody()
                .getSubject()
                .equals(ACCESS_TOKEN_SUBJECT);
    }

    private boolean isNotExpired(final Jws<Claims> claims) {
        return claims
                .getBody()
                .getExpiration()
                .after(new Date());
    }

    private Jws<Claims> getClaimsJws(final String token) {
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token);
    }

    public String getPayload(final String authorizationHeader) {
        final String token = authTokenExtractor.extractToken(authorizationHeader, TOKEN_TYPE);
        return (String) getClaimsJws(token)
                .getBody()
                .get("id");
    }
}
