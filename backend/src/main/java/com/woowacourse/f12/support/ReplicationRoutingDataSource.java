package com.woowacourse.f12.support;

import static com.woowacourse.f12.support.DataSourceType.MASTER;
import static com.woowacourse.f12.support.DataSourceType.SLAVE;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.transaction.support.TransactionSynchronizationManager;

public class ReplicationRoutingDataSource extends AbstractRoutingDataSource {

    @Override
    protected Object determineCurrentLookupKey() {
        if (TransactionSynchronizationManager.isCurrentTransactionReadOnly()) {
            return SLAVE;
        }
        return MASTER;
    }
}
