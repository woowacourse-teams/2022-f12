package com.woowacourse.f12.support;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.IntUnaryOperator;

public class DataSourceLoadBalancer {

    private static final int ROTATE_MIN_NUMBER = 0;

    private final List<DataSourceType> dataSourcePool;
    private final AtomicInteger atomicInteger = new AtomicInteger(ROTATE_MIN_NUMBER);
    private final int rotateMaxNumber;
    private final int rotateMinNumber;
    private final int poolSize;

    public DataSourceLoadBalancer(final List<DataSourceType> dataSourcePool) {
        this.dataSourcePool = dataSourcePool;
        this.poolSize = this.dataSourcePool.size();
        this.rotateMaxNumber = this.poolSize - 1;
        this.rotateMinNumber = ROTATE_MIN_NUMBER;
    }

    public DataSourceType getDataSource() {
        return dataSourcePool.get(calculateIndex());
    }

    private int calculateIndex() {
        return atomicInteger.getAndUpdate(rotateIncrement()) % (poolSize);
    }

    private IntUnaryOperator rotateIncrement() {
        return (it) -> {
            if (it == rotateMaxNumber) {
                return rotateMinNumber;
            }
            return it + 1;
        };
    }
}
