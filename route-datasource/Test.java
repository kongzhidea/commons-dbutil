package com.kk.datasource;

import com.kk.dbaccess.op.DataAccessMgr;
import com.kk.dbaccess.op.OpMap;
import com.kk.utils.log.ConsoleLogger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.sql.SQLException;
import java.util.HashMap;

/**
 * @auther zhihui.kzh
 * @create 2017/1/517:38
 */
public class Test {
    private static ConsoleLogger logger = new ConsoleLogger();

    public static void main(String[] args) throws SQLException {

        ApplicationContext context = new ClassPathXmlApplicationContext("com/kk/datasource/datasource.xml");
        RoutingDataSource dataSource = context.getBean("dataSource",RoutingDataSource.class);

        DataAccessMgr dataAccessMgr = DataAccessMgr.getInstance();

        DataSourceSwitch.setDataSourceType("test1");
        logger.info("op.map=" + dataAccessMgr.queryMap(new OpMap(dataSource,
                "select * from user where id =?", 1)));


    }
}
