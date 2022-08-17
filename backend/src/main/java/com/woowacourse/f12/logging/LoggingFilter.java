package com.woowacourse.f12.logging;

import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

@Slf4j
@Component
public class LoggingFilter extends OncePerRequestFilter {

    private static final String REQUEST_LOG_FORMAT = "METHOD: {}, URL: {}, AUTHORIZATION: {}, BODY: {}";
    private static final String RESPONSE_LOG_FORMAT =
            "STATUS_CODE: {}, METHOD: {}, URL: {}, QUERY_COUNT: {}, TIME_TAKEN: {}ms, BODY: {}";
    private static final String QUERY_COUNT_WARNING_LOG_FORMAT = "쿼리가 {}번 이상 실행되었습니다.";

    private static final int QUERY_COUNT_WARNING_STANDARD = 10;

    private final StopWatch apiTimer;
    private final ApiQueryCounter apiQueryCounter;

    public LoggingFilter(final StopWatch apiTimer, final ApiQueryCounter apiQueryCounter) {
        this.apiTimer = apiTimer;
        this.apiQueryCounter = apiQueryCounter;
    }

    @Override
    protected void doFilterInternal(final HttpServletRequest request, final HttpServletResponse response,
                                    final FilterChain filterChain)
            throws ServletException, IOException {
        final ContentCachingRequestWrapper cachingRequest = new ContentCachingRequestWrapper(request);
        final ContentCachingResponseWrapper cachingResponse = new ContentCachingResponseWrapper(response);

        apiTimer.start();
        filterChain.doFilter(cachingRequest, cachingResponse);
        apiTimer.stop();

        logRequestAndResponse(cachingRequest, cachingResponse);
        cachingResponse.copyBodyToResponse();
    }

    private void logRequestAndResponse(final ContentCachingRequestWrapper request,
                                       final ContentCachingResponseWrapper response) {
        final int queryCount = apiQueryCounter.getCount();
        final String requestBody = new String(request.getContentAsByteArray());
        final String responseBody = new String(response.getContentAsByteArray());

        log.info(REQUEST_LOG_FORMAT, request.getMethod(), request.getRequestURI(),
                request.getHeader("Authorization"), requestBody);
        log.info(RESPONSE_LOG_FORMAT, response.getStatus(), request.getMethod(), request.getRequestURI(),
                queryCount, apiTimer.getLastTaskTimeMillis(), responseBody);
        warnByQueryCount();
    }

    private void warnByQueryCount() {
        final int queryCount = apiQueryCounter.getCount();
        if (queryCount >= QUERY_COUNT_WARNING_STANDARD) {
            log.warn(QUERY_COUNT_WARNING_LOG_FORMAT, QUERY_COUNT_WARNING_STANDARD);
        }
    }
}
