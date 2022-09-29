package com.woowacourse.f12.presentation.auth;

import com.woowacourse.f12.application.auth.JwtProvider;
import com.woowacourse.f12.exception.unauthorized.TokenExpiredException;
import com.woowacourse.f12.exception.unauthorized.TokenNotExistsException;
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
    public boolean preHandle(final HttpServletRequest request, final HttpServletResponse response,
                             final Object handler) {
        if (!(handler instanceof HandlerMethod) || getLoginAnnotation(handler) == null) {
            return true;
        }

        if (hasAuthorization(request)) {
            validateAuthorization(request);
            return true;
        }
        validateTokenRequired(handler);
        return true;
    }

    private void validateTokenRequired(final Object handler) {
        Login auth = getLoginAnnotation(handler);
        if (auth != null && auth.required()) {
            throw new TokenNotExistsException();
        }
    }

    private Login getLoginAnnotation(final Object handler) {
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        return handlerMethod.getMethodAnnotation(Login.class);
    }

    private void validateAuthorization(final HttpServletRequest request) {
        final String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (!jwtProvider.isValidToken(authorizationHeader)) {
            throw new TokenExpiredException();
        }
    }

    private boolean hasAuthorization(final HttpServletRequest request) {
        return request.getHeader(HttpHeaders.AUTHORIZATION) != null;
    }
}
