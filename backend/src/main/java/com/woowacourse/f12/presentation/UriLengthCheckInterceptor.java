package com.woowacourse.f12.presentation;

import com.woowacourse.f12.exception.UriTooLongException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class UriLengthCheckInterceptor implements HandlerInterceptor {

    private static final String QUERY_STRING_DELIMITER = "?";
    private static final int URI_MAXIMUM_LENGTH = 1000;

    @Override
    public boolean preHandle(final HttpServletRequest request, final HttpServletResponse response,
                             final Object handler) {
        if (isUriOverMaximumLength(request)) {
            throw new UriTooLongException(URI_MAXIMUM_LENGTH);
        }
        return true;
    }

    private boolean isUriOverMaximumLength(final HttpServletRequest request) {
        return createUriWithQueryString(request).length() >= URI_MAXIMUM_LENGTH;
    }

    private String createUriWithQueryString(final HttpServletRequest request) {
        final String requestURL = request.getRequestURI();
        final String queryString = request.getQueryString();
        if (queryString == null) {
            return requestURL;
        }
        return requestURL + QUERY_STRING_DELIMITER + queryString;
    }
}
