package com.woowacourse.f12.logging;

import java.util.Objects;
import org.hibernate.resource.jdbc.spi.StatementInspector;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;

@Component
public class ApiQueryInspector implements StatementInspector {

    private final QueryCounter queryCounter;

    public ApiQueryInspector(final QueryCounter queryCounter) {
        this.queryCounter = queryCounter;
    }

    @Override
    public String inspect(final String sql) {
        if (isInRequestScope()) {
            queryCounter.increaseCount();
        }
        return sql;
    }

    private boolean isInRequestScope() {
        return Objects.nonNull(RequestContextHolder.getRequestAttributes());
    }

    public int getQueryCount() {
        return queryCounter.getCount();
    }
}
