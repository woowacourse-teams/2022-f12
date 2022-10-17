package com.woowacourse.f12.support;

import static com.woowacourse.f12.support.DataSourceType.MASTER;
import static com.woowacourse.f12.support.DataSourceType.SLAVE;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.transaction.support.TransactionSynchronizationManager;

@Slf4j
public class ReplicationRoutingDataSource extends AbstractRoutingDataSource {

    @Override
    protected Object determineCurrentLookupKey() {
        final String transactionName = TransactionSynchronizationManager.getCurrentTransactionName();
        final boolean readOnly = TransactionSynchronizationManager.isCurrentTransactionReadOnly();
        if (transactionName == null) {
            return MASTER;
        }
        if (readOnly) {
            log.info("TRANSACTION_NAME : {}, DATASOURCE : {}", transactionName, SLAVE);
            return SLAVE;
        }
        log.info("TRANSACTION_NAME : {}, DATASOURCE : {}", transactionName, MASTER);
        return MASTER;
    }
}
