package com.woowacourse.f12.logging;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.resource.jdbc.spi.StatementInspector;
import org.springframework.beans.factory.support.ScopeNotActiveException;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class QueryInspector implements StatementInspector {

    private static final String QUERY_LOG_FORMAT = "QUERY: {}";

    private final QueryCounter queryCounter;

    public QueryInspector(final QueryCounter queryCounter) {
        this.queryCounter = queryCounter;
    }

    @Override
    public String inspect(final String sql) {
        try {
            queryCounter.increaseCount();
            log.info(QUERY_LOG_FORMAT, sql);
        } catch (ScopeNotActiveException ignored) {
        }
        return sql;
    }

    public int getQueryCount() {
        return queryCounter.getCount();
    }
}
