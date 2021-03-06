package com.woowacourse.f12.support;

import com.woowacourse.f12.exception.UnauthorizedException;
import java.util.Objects;
import org.springframework.stereotype.Component;

@Component
public class AuthTokenExtractor {

    public String extractToken(final String authorizationHeader, final String tokenType) {
        if (Objects.isNull(authorizationHeader)) {
            throw new UnauthorizedException();
        }
        final String[] splitHeaders = authorizationHeader.split(" ");
        if (splitHeaders.length != 2 || !splitHeaders[0].equalsIgnoreCase(tokenType)) {
            throw new UnauthorizedException();
        }
        return splitHeaders[1];
    }
}
