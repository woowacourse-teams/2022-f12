package com.woowacourse.f12.presentation.auth;

import com.woowacourse.f12.application.auth.JwtProvider;
import com.woowacourse.f12.exception.unauthorized.TokenExpiredException;
import java.util.Objects;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class AuthInterceptor implements HandlerInterceptor {

    private final JwtProvider jwtProvider;

    public AuthInterceptor(final JwtProvider jwtProvider) {
        this.jwtProvider = jwtProvider;
    }

    @Override
    public boolean preHandle(final HttpServletRequest request, final HttpServletResponse response, final Object handler) {
        if (isNotTarget(handler)) {
            return true;
        }
        validateAuthorization(request);
        return true;
    }

    private boolean isNotTarget(final Object handler) {
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        LoginRequired auth = handlerMethod.getMethodAnnotation(LoginRequired.class);
        return Objects.isNull(auth);
    }

    private void validateAuthorization(final HttpServletRequest request) {
        final String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (!jwtProvider.validateToken(authorizationHeader)) {
            throw new TokenExpiredException();
        }
    }
}
