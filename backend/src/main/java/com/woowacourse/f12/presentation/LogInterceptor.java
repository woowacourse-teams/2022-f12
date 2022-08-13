package com.woowacourse.f12.presentation;

import com.woowacourse.f12.support.RequestLogTimer;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

@Slf4j
@Component
public class LogInterceptor implements HandlerInterceptor {

    private static final String REQUEST_LOG_FORMAT = "METHOD: {}, URL : {}, AUTHORIZATION : {}, BODY : {}";
    private static final String RESPONSE_LOG_FORMAT = "STATUS_CODE: {}, URL : {}, BODY : {}, TIME_TAKEN : {}";

    private final RequestLogTimer requestLogTimer;

    public LogInterceptor(final RequestLogTimer requestLogTimer) {
        this.requestLogTimer = requestLogTimer;
    }

    @Override
    public boolean preHandle(final HttpServletRequest request, final HttpServletResponse response, final Object handler)
            throws Exception {
        requestLogTimer.start();

        final String body = new String(new ContentCachingRequestWrapper(request).getContentAsByteArray());
        log.info(REQUEST_LOG_FORMAT, request.getMethod(), request.getRequestURI(), request.getHeader("Authorization"),
                body);
        return true;
    }

    @Override
    public void afterCompletion(final HttpServletRequest request, final HttpServletResponse response,
                                final Object handler, final Exception ex)
            throws Exception {
        requestLogTimer.stop();

        final ContentCachingResponseWrapper contentCachingResponseWrapper = new ContentCachingResponseWrapper(response);
        final String responseBody = new String(contentCachingResponseWrapper.getContentAsByteArray());
        log.info(RESPONSE_LOG_FORMAT, response.getStatus(), request.getRequestURI(), responseBody,
                requestLogTimer.getSpentTime());
    }
}
