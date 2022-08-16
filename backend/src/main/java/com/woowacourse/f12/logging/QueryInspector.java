package com.woowacourse.f12.logging;

import org.hibernate.resource.jdbc.spi.StatementInspector;
import org.springframework.beans.factory.support.ScopeNotActiveException;
import org.springframework.stereotype.Component;

@Component
public class QueryInspector implements StatementInspector {

    private final QueryCounter queryCounter;

    public QueryInspector(final QueryCounter queryCounter) {
        this.queryCounter = queryCounter;
    }

    @Override
    public String inspect(final String sql) {
        try {
            queryCounter.increaseCount();
        } catch (ScopeNotActiveException ignored) {
        }
        return sql;
    }

    public int getQueryCount() {
        return queryCounter.getCount();
    }
}
