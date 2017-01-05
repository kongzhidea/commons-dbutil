package com.kk.datasource;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

/**
 * 最好在spring中使用，父类中实现InitializingBean，在spring中初始化的时候会调用afterPropertiesSet
 *
 */
public class RoutingDataSource extends AbstractRoutingDataSource {
    @Override
    protected Object determineCurrentLookupKey() {
        return DataSourceSwitch.getDataSourceType();
    }
}
