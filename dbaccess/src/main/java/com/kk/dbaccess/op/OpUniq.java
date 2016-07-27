package com.kk.dbaccess.op;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class OpUniq extends AbstractOp {
    private Object result;

    public OpUniq() {
    }

    public OpUniq(DataSource dataSource, String sql) {
        this.dataSource = dataSource;
        this.sql = sql;
    }

    public OpUniq(DataSource dataSource, String sql, Object... params) {
        this.dataSource = dataSource;
        this.sql = sql;
        this.params = params;
    }

    public final Object getResult() {
        return result;
    }

    public final void add(Object ob) {
        result = ob;
    }
}