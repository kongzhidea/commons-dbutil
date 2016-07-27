package com.kk.dbaccess.op;

import com.kk.dbaccess.op.mapper.TemplateRowMapper;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * parse转成bean， 支持下划线与驼峰式写法的转换
 *
 * @param <T>
 */
public class OpBeanUniq<T> extends AbstractOp {
    private Class<T> clz;
    private T result;

    public OpBeanUniq(Class<T> clz) {
        this.clz = clz;
    }

    public OpBeanUniq(Class<T> clz, DataSource dataSource, String sql) {
        this.clz = clz;
        this.dataSource = dataSource;
        this.sql = sql;
    }

    public OpBeanUniq(Class<T> clz, DataSource dataSource, String sql, Object... params) {
        this.clz = clz;
        this.dataSource = dataSource;
        this.sql = sql;
        this.params = params;
    }

    public final T getResult() {
        return result;
    }

    public final void add(T ob) {
        result = ob;
    }

    // 重写此方法
    public T parse(ResultSet rs) throws SQLException {
        TemplateRowMapper<T> rowMapper = new TemplateRowMapper<T>(clz);
        return rowMapper.mapRow(rs);
    }

}