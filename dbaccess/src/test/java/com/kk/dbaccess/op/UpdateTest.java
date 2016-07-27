package com.kk.dbaccess.op;

import org.junit.Test;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;

public class UpdateTest extends BaseTest {
    @Test
    public void testUpdate() throws SQLException {
        String sql = "update user set privs=2 where id = ?";
        int ret = dataAccessMgr.update(new OpUpdate(dataSource, sql, 195));

        logger.info(ret);
    }
}
