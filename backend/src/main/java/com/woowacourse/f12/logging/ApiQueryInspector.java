package com.woowacourse.f12.logging;

import java.util.Objects;
import org.hibernate.resource.jdbc.spi.StatementInspector;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;

@Component
public class ApiQueryInspector implements StatementInspector {

    private final ApiQueryCounter apiQueryCounter;

    public ApiQueryInspector(final ApiQueryCounter apiQueryCounter) {
        this.apiQueryCounter = apiQueryCounter;
    }

    @Override
    public String inspect(final String sql) {
        if (isInRequestScope()) {
            apiQueryCounter.increaseCount();
        }
        return sql;
    }

    private boolean isInRequestScope() {
        return Objects.nonNull(RequestContextHolder.getRequestAttributes());
    }

    public int getQueryCount() {
        return apiQueryCounter.getCount();
    }
}
