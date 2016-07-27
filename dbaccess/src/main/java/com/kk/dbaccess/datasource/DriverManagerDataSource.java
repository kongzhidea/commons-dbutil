package com.kk.dbaccess.datasource;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DriverManagerDataSource extends AbstractDriverBasedDataSource {
    public DriverManagerDataSource() {
    }

    public DriverManagerDataSource(String url) {
        this.setUrl(url);
    }

    public DriverManagerDataSource(String url, String username, String password) {
        this.setUrl(url);
        this.setUsername(username);
        this.setPassword(password);
    }

    public DriverManagerDataSource(String url, Properties conProps) {
        this.setUrl(url);
        this.setConnectionProperties(conProps);
    }

    public void setDriverClassName(String driverClassName) {
        String driverClassNameToUse = driverClassName.trim();

        try {
            Class.forName(driverClassNameToUse, true, getDefaultClassLoader());
        } catch (ClassNotFoundException var4) {
            throw new IllegalStateException("Could not load JDBC driver class [" + driverClassNameToUse + "]", var4);
        }

        if (this.logger.isInfoEnabled()) {
            this.logger.info("Loaded JDBC driver: " + driverClassNameToUse);
        }

    }

    public static ClassLoader getDefaultClassLoader() {
        ClassLoader cl = null;
        try {
            cl = Thread.currentThread().getContextClassLoader();
        } catch (Throwable ex) {
            // Cannot access thread context ClassLoader - falling back...
        }
        if (cl == null) {
            // No thread context class loader -> use class loader of this class.
            cl = DriverManagerDataSource.class.getClassLoader();
            if (cl == null) {
                // getClassLoader() returning null indicates the bootstrap ClassLoader
                try {
                    cl = ClassLoader.getSystemClassLoader();
                } catch (Throwable ex) {
                    // Cannot access system ClassLoader - oh well, maybe the caller can live with null...
                }
            }
        }
        return cl;
    }

    protected Connection getConnectionFromDriver(Properties props) throws SQLException {
        String url = this.getUrl();
        if (this.logger.isDebugEnabled()) {
            this.logger.debug("Creating new JDBC DriverManager Connection to [" + url + "]");
        }

        return this.getConnectionFromDriverManager(url, props);
    }

    protected Connection getConnectionFromDriverManager(String url, Properties props) throws SQLException {
        return DriverManager.getConnection(url, props);
    }
}
