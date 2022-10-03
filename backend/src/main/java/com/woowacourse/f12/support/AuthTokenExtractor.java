package com.woowacourse.f12.support;

import com.woowacourse.f12.exception.unauthorized.TokenInvalidFormatException;
import com.woowacourse.f12.exception.unauthorized.TokenNotExistsException;
import org.springframework.stereotype.Component;

@Component
public class AuthTokenExtractor {

    public String extractToken(final String authorizationHeader, final String tokenType) {
        if (authorizationHeader == null) {
            throw new TokenNotExistsException();
        }
        final String[] splitHeaders = authorizationHeader.split(" ");
        if (splitHeaders.length != 2 || !splitHeaders[0].equalsIgnoreCase(tokenType)) {
            throw new TokenInvalidFormatException();
        }
        return splitHeaders[1];
    }
}
