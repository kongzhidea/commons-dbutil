package com.kk.dbaccess.op;

import com.kk.dbaccess.util.JdbcUtils;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AbstractOp extends Op {
    protected Object[] params;

    public Object[] getParams() {
        return params;
    }

    public void setParams(Object[] params) {
        this.params = params;
    }

    // 设置param
    @Override
    public void setParam(PreparedStatement ps) throws SQLException {
        fillStatement(ps, params);
    }

    // 将rs转成需要的类型， 如果是获取一个值并且是基本类型，可以选择覆写此方法，也可以在DataAccess中直接使用parse(Rs,Class)方法
    @Override
    public Object parse(ResultSet rs) throws SQLException {
        return null;
    }

    public Object parse(ResultSet rs, Class<?> clz) throws SQLException {
        return JdbcUtils.getResultSetValue(rs, 1, clz);
    }
}
