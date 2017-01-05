package com.kk.datasource;


public class DataSourceSwitch {

    private static final ThreadLocal<String> contextHolder = new ThreadLocal<String>();

    // 设置数据源，在xml中配置的targetDataSources，如果不设置或者type不存在，则使用 defaultTargetDataSource
    public static void setDataSourceType(String dataSourceType) {
        contextHolder.set(dataSourceType);
    }

    public static String getDataSourceType() {
        return contextHolder.get();
    }

    public static void clearDataSourceType() {
        contextHolder.remove();
    }
}
