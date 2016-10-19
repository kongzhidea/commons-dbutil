package com.kk.dbaccess.op;

import com.kk.dbaccess.datasource.DriverManagerDataSource;
import com.kk.dbaccess.util.ConsoleLogger;
import com.kk.dbaccess.util.Consts;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Before;

import javax.sql.DataSource;

public class BaseTest {
    ConsoleLogger logger = new ConsoleLogger();

    DataAccessMgr dataAccessMgr = DataAccessMgr.getInstance();

    DriverManagerDataSource dataSource;

    @Before
    public void init() {
        dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(Consts.driverClassName);
        dataSource.setUrl(Consts.url);
        dataSource.setUsername(Consts.username);
        dataSource.setPassword(Consts.password);
    }

}
