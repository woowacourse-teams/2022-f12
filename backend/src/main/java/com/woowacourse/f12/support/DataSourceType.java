package com.woowacourse.f12.support;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public enum DataSourceType {

    MASTER(false, 1),
    SLAVE(true, 1),
    SLAVE_SUB(true, 1);

    private final boolean isSlave;
    private final int ratio;

    DataSourceType(final boolean isSlave, final int ratio) {
        this.isSlave = isSlave;
        this.ratio = ratio;
    }

    public static List<DataSourceType> getSlaveDataSourcePool() {
        final List<DataSourceType> dataSourceTypes = Arrays.stream(DataSourceType.values())
                .filter(it -> it.isSlave)
                .collect(Collectors.toList());
        List<DataSourceType> dataSources = new ArrayList<>();
        for (DataSourceType dataSourceType : dataSourceTypes) {
            for (int i = 0; i < dataSourceType.getRatio(); i++) {
                dataSources.add(dataSourceType);
            }
        }
        return dataSources;
    }

    public int getRatio() {
        return ratio;
    }
}
