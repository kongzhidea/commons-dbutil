package com.kk.dbaccess.op;

import com.kk.dbaccess.op.mapper.SingleColumnRowMapper;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class OpUniq<T> extends AbstractOp {
    private Class<T> clz;
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

    public OpUniq(Class<T> clz) {
        this.clz = clz;
    }

    public OpUniq(Class<T> clz, DataSource dataSource, String sql) {
        this.clz = clz;
        this.dataSource = dataSource;
        this.sql = sql;
    }

    public OpUniq(Class<T> clz, DataSource dataSource, String sql, Object... params) {
        this.clz = clz;
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


    // 返回bean则直接使用OpBeanUniq

    // 如果是返回int,string等类型，则不用覆写 parse方法，只需要调用 queryInt等方法即可，内部对rs已经做了处理
    
    // 如果是返回int,string等类型，可以使用queryObject，设置泛型即可，最好指定clz，否则返回值一定要和数据库中的column值类型相对应
    @Override
    public T parse(ResultSet rs) throws SQLException {
        SingleColumnRowMapper<T> rowMapper = new SingleColumnRowMapper<T>(clz);
        return rowMapper.mapRow(rs);
    }
}