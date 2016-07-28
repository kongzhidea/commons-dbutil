package com.kk.dbaccess.op;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class OpUpdate extends AbstractOp {

    public OpUpdate() {
    }

    public OpUpdate(DataSource dataSource, String sql) {
        this.dataSource = dataSource;
        this.sql = sql;
    }

    public OpUpdate(DataSource dataSource, String sql, Object... params) {
        this.dataSource = dataSource;
        this.sql = sql;
        this.params = params;
    }
    // 不用覆写 parse方法，access中一般使用update的时候，只关注更新成功的数量。
}