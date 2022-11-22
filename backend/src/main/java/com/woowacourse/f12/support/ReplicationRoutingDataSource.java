package com.woowacourse.f12.support;

import static com.woowacourse.f12.support.DataSourceType.MASTER;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.transaction.support.TransactionSynchronizationManager;

@Slf4j
public class ReplicationRoutingDataSource extends AbstractRoutingDataSource {

    private final DataSourceLoadBalancer dataSourceLoadBalancer;

    public ReplicationRoutingDataSource(final DataSourceLoadBalancer dataSourceLoadBalancer) {
        this.dataSourceLoadBalancer = dataSourceLoadBalancer;
    }

    @Override
    protected Object determineCurrentLookupKey() {
        final String transactionName = TransactionSynchronizationManager.getCurrentTransactionName();
        if (transactionName == null) {
            return MASTER;
        }
        final boolean readOnly = TransactionSynchronizationManager.isCurrentTransactionReadOnly();
        if (readOnly) {
            final DataSourceType dataSourceType = dataSourceLoadBalancer.getDataSource();
            log.info("TRANSACTION_NAME : {}, DATASOURCE : {}", transactionName, dataSourceType);
            return dataSourceType;
        }
        log.info("TRANSACTION_NAME : {}, DATASOURCE : {}", transactionName, MASTER);
        return MASTER;
    }
}
