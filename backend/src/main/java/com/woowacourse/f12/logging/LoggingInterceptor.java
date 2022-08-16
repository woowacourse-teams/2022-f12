package com.woowacourse.f12.logging;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

@Slf4j
@Component
public class LoggingInterceptor implements HandlerInterceptor {

    private static final String REQUEST_LOG_FORMAT = "METHOD: {}, URL: {}, AUTHORIZATION: {}, BODY: {}";
    private static final String QUERY_COUNT_LOG_FORMAT = "QUERY_COUNT: {}";
    private static final String RESPONSE_LOG_FORMAT = "STATUS_CODE: {}, URL: {}, TIME_TAKEN: {}ms, BODY: {}";
    private static final String QUERY_COUNT_WARNING_LOG_FORMAT = "쿼리가 {}번 이상 실행되었습니다.";

    private static final int QUERY_COUNT_WARNING_STANDARD = 10;

    private final StopWatch apiTimer;
    private final ApiQueryCounter apiQueryCounter;

    public LoggingInterceptor(final StopWatch apiTimer, final ApiQueryCounter apiQueryCounter) {
        this.apiTimer = apiTimer;
        this.apiQueryCounter = apiQueryCounter;
    }

    @Override
    public boolean preHandle(final HttpServletRequest request, final HttpServletResponse response,
                             final Object handler) {
        apiTimer.start();

        final String body = new String(new ContentCachingRequestWrapper(request).getContentAsByteArray());

        log.info(REQUEST_LOG_FORMAT, request.getMethod(), request.getRequestURI(), request.getHeader("Authorization"),
                body);
        return true;
    }

    @Override
    public void afterCompletion(final HttpServletRequest request, final HttpServletResponse response,
                                final Object handler, final Exception ex) {
        apiTimer.stop();

        final ContentCachingResponseWrapper contentCachingResponseWrapper = new ContentCachingResponseWrapper(response);
        final String responseBody = new String(contentCachingResponseWrapper.getContentAsByteArray());
        final int queryCount = apiQueryCounter.getCount();

        log.info(QUERY_COUNT_LOG_FORMAT, queryCount);
        if (queryCount >= QUERY_COUNT_WARNING_STANDARD) {
            log.warn(QUERY_COUNT_WARNING_LOG_FORMAT, QUERY_COUNT_WARNING_STANDARD);
        }
        log.info(RESPONSE_LOG_FORMAT, response.getStatus(), request.getRequestURI(), apiTimer.getLastTaskTimeMillis(),
                responseBody);
    }
}
