package com.kk.dbaccess.op;

import org.junit.Before;
import org.junit.Test;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;

public class InsertTest extends BaseTest {
    @Test
    public void testInsert() throws SQLException {
        String sql = "insert user(username,realname,ctime) values(?,?,?)";
        boolean ret = dataAccessMgr.insert(new OpUpdate(dataSource, sql) {
            @Override
            public void setParam(PreparedStatement ps) throws SQLException {
                ps.setString(1, "_op_1");
                ps.setString(2, "kk");
                ps.setObject(3, new Date());
            }
        });

        logger.info(ret);
    }

    @Test
    public void testInsert2() throws SQLException {
        String sql = "insert user(username,realname,ctime) values(?,?,?)";
        boolean ret = dataAccessMgr.insert(new OpUpdate(dataSource, sql, "_op_2", "kkl", new Date()));

        logger.info(ret);
    }

    @Test
    public void testInsertReturnId() throws SQLException {
        String sql = "insert user(username,realname,ctime) values(?,?,?)";
        long id = dataAccessMgr.insertReturnId(new OpUpdate(dataSource, sql, "_op_3", "kkl", new Date()));

        logger.info(id);
    }
}
