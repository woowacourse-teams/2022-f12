package com.woowacourse.f12.logging;

import java.util.Objects;
import org.hibernate.resource.jdbc.spi.StatementInspector;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;

@Component
public class QueryInspector implements StatementInspector {

    private final QueryCounter queryCounter;

    public QueryInspector(final QueryCounter queryCounter) {
        this.queryCounter = queryCounter;
    }

    @Override
    public String inspect(final String sql) {
        if (isInRequest()) {
            queryCounter.increaseCount();
        }
        return sql;
    }

    private boolean isInRequest() {
        return Objects.nonNull(RequestContextHolder.getRequestAttributes());
    }

    public int getQueryCount() {
        return queryCounter.getCount();
    }
}
