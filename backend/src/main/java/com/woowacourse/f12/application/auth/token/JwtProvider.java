package com.woowacourse.f12.application.auth.token;

import com.woowacourse.f12.domain.member.Role;
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
import java.util.Map;
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

    public String createAccessToken(final Long id, final Role role) {
        final Date now = new Date();
        final Date validity = new Date(now.getTime() + validityInMilliseconds);
        final Map<String, Object> claims = Map.of("id", id.toString(), "role", role);

        return Jwts.builder()
                .setSubject(ACCESS_TOKEN_SUBJECT)
                .setIssuedAt(now)
                .setExpiration(validity)
                .addClaims(claims)
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

    public MemberPayload getPayload(final String authorizationHeader) {
        final String token = authTokenExtractor.extractToken(authorizationHeader, TOKEN_TYPE);
        Claims body = getClaimsJws(token)
                .getBody();
        String id = body.get("id", String.class);
        Role role = Role.valueOf(body.get("role", String.class));
        return new MemberPayload(Long.parseLong(id), role);
    }
}
